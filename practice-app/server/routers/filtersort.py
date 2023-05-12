import json
from fastapi import APIRouter
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
import requests

headers = {
    'X-API-Key': '1234567890ASDFGHJK',
    "accept": "application/json"
}

# Create a new FastAPI router
router = APIRouter()

db = MongoDB.getInstance()

#external api usage
@router.get("/aqdata")
async def get_measurements(country=None, city=None, sort="asc"):
    # Define the base URL of the OpenAQ API
    url_prefix = "https://api.openaq.org/v2/latest?limit=10&page=1&offset=0"
    country_filter = ""
    city_filter = ""
    if country:
        country_filter = f"&country_id={country}"
    if city:
        city_filter = f"&city={city}"
    sort_direction = f"&sort={sort}"
    url_suffix = f"&order_by=lastUpdated&dumpRaw=false"
    url = url_prefix + country_filter + city_filter + sort_direction + url_suffix
    response = requests.get(url, headers=headers)

    if response.status_code == 200:  # Successful
        data = json.loads(response.text)
        aqdata = data["results"]
        output_data = []
        for record in aqdata:
            measurements = record['measurements']
            for measurement in measurements:
                output_data.append({
                    'country': record['country'],
                    'city': record['city'],
                    'measurement': measurement['parameter'],
                    'lastUpdated': measurement['lastUpdated']
                })
        # Example filtering: filter by country
        if country:
            output_data = [data for data in output_data if data["country"].lower() == country.lower()]

        # Example filtering: filter by city
        if city:
            output_data = [data for data in output_data if data["city"].lower() == city.lower()]

        # Example sorting: sort by lastUpdated in ascending or descending order
        if sort == "asc":
            output_data = sorted(output_data, key=lambda data: data["lastUpdated"])
        elif sort == "desc":
            output_data = sorted(output_data, key=lambda data: data["lastUpdated"], reverse=True)

        return output_data

    else:
        raise Exception(f"Request to OpenAQ API failed with status code {response.status_code}.")
