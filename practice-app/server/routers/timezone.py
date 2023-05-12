from fastapi import APIRouter
from config import Config
from pydantic import BaseModel
import requests
from datetime import datetime

router = APIRouter()


class Location(BaseModel):
    lat: float
    lng: float


@router.post('/get_timezone')
async def get_timezone(location: Location):
    """
    Convert latitude and longitude data to timezone and date.
    """
    print(location.lat, location.lng)
    api_key = Config.TIMEZONE_API_KEY
    url = f'http://api.timezonedb.com/v2.1/get-time-zone?key={api_key}&format=json&by=position&lat={location.lat}&lng={location.lng}'
    response = requests.get(url).json()

    if response['status'] == 'OK':
        timezone = response['zoneName']
        date = datetime.utcfromtimestamp(
            response['timestamp']).strftime('%Y-%m-%d %H:%M:%S')
        return {'timezone': timezone, 'date': date}
    else:
        return {'error': 'Could not get timezone data'}
