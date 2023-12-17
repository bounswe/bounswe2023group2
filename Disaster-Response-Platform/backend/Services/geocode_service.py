from Database.mongo import MongoDB
from bson.objectid import ObjectId
from pymongo import ASCENDING, DESCENDING
from Services.build_API_returns import *
from typing import Optional
from datetime import datetime, timedelta
import requests
import config

GOOGLE_MAPS_API_KEY = config.GOOGLE_MAPS_API_KEY

# Utilizes Google Maps Places API to get first autocomplete suggestion for a given address input.
def get_address_suggestion(input_address):
    base_url = f"https://maps.googleapis.com/maps/api/place/autocomplete/json?input={input_address}&key={GOOGLE_MAPS_API_KEY}"
    response = requests.get(base_url)
    if response.status_code == 200:
        suggestions = response.json().get("predictions", [])
        if suggestions:
            # Return the first suggestion's description
            return suggestions[0].get("description")
        else:
            return None
    else:
        return None


# Geocode. Converts address to latitude and longitude.
def convert_address_to_coordinates(address):
    url = f"https://maps.googleapis.com/maps/api/geocode/json?address={address}&key={GOOGLE_MAPS_API_KEY}"
    response = requests.get(url)
    if response.status_code == 200:
        results = response.json().get("results", [])
        if results:
            location = results[0].get("geometry", {}).get("location", {})
            return location.get("lat"), location.get("lng")
        else:
            return None
    else:
        return None


# Gets input address, autocompletes it to a valid address and converts to coordinates.
def autocomplete_address_and_convert_to_coordinates(input_address):
    complete_address = get_address_suggestion(input_address)
    if complete_address:
        print(f"Complete Address: {complete_address}")
        # Get coordinates of the complete address
        coordinates = geocode(complete_address, api_key)
        
        if coordinates:
            return coordinates
        else:
            return None
    else:
        return None


# Reverse geocode. Converts latitude, longitude to a formatted adress.
def convert_coordinates_to_address(latitude, longitude):
    url = f"https://maps.googleapis.com/maps/api/geocode/json?latlng={latitude},{longitude}&key={GOOGLE_MAPS_API_KEY}"
    response = requests.get(url)
    if response.status_code == 200:
        results = response.json().get("results", [])
        if results:
            return results[0].get("formatted_address")
        else:
            return None
    else:
        return None