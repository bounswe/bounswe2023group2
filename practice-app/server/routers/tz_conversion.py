from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from pytz import timezone
from datetime import datetime
from typing import List, Dict
from config import Config
import requests
import pymongo
from timezonefinder import TimezoneFinder

router = APIRouter()

# connect to MongoDB
client = pymongo.MongoClient(Config.MONGO_URI)
db = client.get_default_database()
conversions = db["conversions"]

# get timezone data from Timezone DB API
tz_data = requests.get(
    f'http://api.timezonedb.com/v2.1/list-time-zone?key={Config.TIMEZONE_API_KEY}&format=json').json()


@router.get("/list_timezones")
async def list_timezones() -> List[str]:
    """
    Retrieve a list of all available timezones from the Timezone DB API.
    """
    return [tz["zoneName"] for tz in tz_data["zones"]]



class Location(BaseModel):
    time: str
    from_tz: str
    to_tz: str

@router.post("/convert_time")
async def convert_time(
    location:Location
) -> Dict[str, str]:
    """
    Convert a given time from one timezone to another.
    """
    tf = TimezoneFinder()
    from_lat, from_lon = tf.timezone_at(place=location.from_tz)
    to_lat, to_lon = tf.timezone_at(place=location.to_tz)
    if not all([from_lat, from_lon, to_lat, to_lon]):
        raise HTTPException(status_code=400, detail="Invalid timezone(s)")

    from_tz = timezone(tf.timezone_at(lng=from_lon, lat=from_lat))
    to_tz = timezone(tf.timezone_at(lng=to_lon, lat=to_lat))
    try:
        converted_time = from_tz.localize(
            datetime.strptime(location.time, "%Y-%m-%d %H:%M:%S")
        ).astimezone(to_tz)
    except ValueError:
        raise HTTPException(status_code=400, detail="Invalid time")

    # save the conversion in the MongoDB database
    conversion = {
        "original_time": location.time,
        "converted_time": converted_time.strftime("%Y-%m-%d %H:%M:%S"),
        "from_tz": from_tz.zone,
        "to_tz": to_tz.zone,
        "conversion_name": f"{from_tz.zone} to {to_tz.zone} at {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}"
    }
    conversions.insert_one(conversion)

    return {
        "original_time": location.time,
        "converted_time": converted_time.strftime("%Y-%m-%d %H:%M:%S"),
        "from_tz": from_tz.zone,
        "to_tz": to_tz.zone,
    }


@router.get("/saved_conversions")
async def saved_conversions() -> List[Dict[str, str]]:
    """
    Retrieve a list of saved time conversions from the MongoDB database.
    """
    return [conv for conv in conversions.find()]


class Conversion(BaseModel):
    conversion_name:str

@router.post("/delete_conversion")
async def delete_conversion(conversion:Conversion) -> Dict[str, str]:
    """
    Delete a saved time conversion from the MongoDB database.
    """
    result = conversions.delete_one({"conversion_name": conversion.conversion_name})
    if result.deleted_count == 0:
        raise HTTPException(status_code=404, detail="Conversion not found")
    return {"message": f"Deleted {result.deleted_count} conversion(s)"}
