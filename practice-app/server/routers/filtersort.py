import json
from fastapi import APIRouter, Query, Body
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
from database.resourceSchema import ResourceSchema
import requests
from typing import Dict
from fastapi import Query
import asyncio

headers = {
    'X-API-Key': '1234567890ASDFGHJK',
    "accept": "application/json"
}

# Create a new FastAPI router
router = APIRouter()

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

# Get the MongoDB instance
db = MongoDB.getInstance()

@router.get("/resources")
async def filter_and_sort_resources_mongo(
    id: str = None,
    type: str = None,
    location: str = None,
    notes: str = None,
    updated_at: str = None,
    is_active: bool = None,
    upvotes: int = None,
    downvotes: int = None,
    creator_id: str = None,
    creation_date: str = None,
    condition: str = None,
    quantity: int = None,
    sort_by: str = ""
):
    # Get the Resources collection
    resources = db.get_collection('Resources')

    # Make sure the sort_by field is valid
    if sort_by not in ResourceSchema.fields:
        print(f"Invalid field: {sort_by}")
        raise ValueError(f"Invalid field: {sort_by}")

    # Build the filter dictionary
    filter_dict = {}
    if id:
        filter_dict["id"] = id
    if type:
        filter_dict["type"] = type
    if location:
        filter_dict["location"] = location
    if notes:
        filter_dict["notes"] = notes
    if updated_at:
        filter_dict["updated_at"] = updated_at
    if is_active is not None:
        filter_dict["is_active"] = is_active
    if upvotes is not None:
        filter_dict["upvotes"] = upvotes
    if downvotes is not None:
        filter_dict["downvotes"] = downvotes
    if creator_id:
        filter_dict["creator_id"] = creator_id
    if creation_date:
        filter_dict["creation_date"] = creation_date
    if condition:
        filter_dict["condition"] = condition
    if quantity is not None:
        filter_dict["quantity"] = quantity

    # Query the collection and retrieve all documents as a list
    results = list(resources.find(filter_dict))

    # Sort the results based on the sort_by field
    if sort_by:
        results = sorted(results, key=lambda resource: resource.get(sort_by))

    # Use the ResourceSchema to convert the results to the expected format
    return [ResourceSchema.dump(resource) for resource in results]


@router.post("/resources")
async def create_resource(resource: dict):
    # Insert the new resource into the collection
    resource_id = ResourceSchema.add_resource(resource)
    # Get the Resources collection
    resources = db.get_collection('Resources')
    # Retrieve the newly created resource
    new_resource = resources.find_one({"id": resource_id})
    # Return the newly created resource
    return new_resource

@router.delete("/resources/{resource_id}")
async def delete_resource(resource_id: str):
    # Delete the resource from the collection
    deleted_count = ResourceSchema.delete_resource(resource_id)
    # Get the Resources collection
    resources = db.get_collection('Resources')
    if deleted_count > 0:
        return {"message": f"Resource with ID {resource_id} deleted successfully."}
    else:
        return {"message": f"Resource with ID {resource_id} not found."}

@router.delete("/resources/clear")
async def clear_resources():
    # Clear all resources from the collection
    result = ResourceSchema.clear_resources()
    # Get the Resources collection
    resources = db.get_collection('Resources')
    return {"message": result["message"]}

