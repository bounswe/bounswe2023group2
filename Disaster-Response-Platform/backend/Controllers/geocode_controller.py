from fastapi import APIRouter, Depends, HTTPException, Body, Query
from Services.geocode_service import autocomplete_address_and_convert_to_coordinates, convert_coordinates_to_address

router = APIRouter()

@router.get("/address_to_coordinates",)
def autocomplete_address(address: str = Query(..., description="Input address to autocomplete and convert to coordinates")):
    """
    Autocompletes the input address and returns the complete address along with its coordinates.
    """
    coordinates = autocomplete_address_and_convert_to_coordinates(address)
    if coordinates:
        latitude, longitude = coordinates
        formatted_address = convert_coordinates_to_address(latitude, longitude)
        return {"address": address, 
                "complete_address": formatted_address, 
                "latitude": latitude, 
                "longitude": longitude, 
                "x": latitude, 
                "y": longitude}
    else:
        raise HTTPException(status_code=404, detail="Address not found")


@router.get("/coordinates_to_address")
def convert_coordinates(latitude: float = Query(..., description="Latitude (x) of the location"),
                        longitude: float = Query(..., description="Longitude (y) of the location")):
    """
    Converts latitude and longitude to a formatted address.
    """
    formatted_address = convert_coordinates_to_address(latitude, longitude)
    if formatted_address:
        return {"latitude": latitude, 
                "longitude": longitude, 
                "address": formatted_address}
    else:
        raise HTTPException(status_code=404, detail="Coordinates not found")
