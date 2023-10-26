from Models.resource_model import Resource
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *

# Get the resources collection using the MongoDB class
resources_collection = MongoDB.get_collection('resources')

def create_resource(resource: Resource) -> str:
    # Manual validation for required fields during creation
    if not all([resource._id, resource.created_by, resource.condition, 
                resource.initialQuantity, resource.currentQuantity,
                resource.type, resource.details]):
        raise ValueError("All fields are mandatory for creation.")
    insert_result = resources_collection.insert_one(resource.dict())
    if insert_result.inserted_id:
        return "{\"resources\":[" + json.dumps(dict(resource)) + "], \"inserted_id\": " + f"\"{insert_result.inserted_id}\"" + "}"
    else:
        raise ValueError("Resource could not be created")

def get_resource_by_id(resource_id: str) -> list[dict]:
    return get_resources(resource_id)

def get_resources(resource_id:str = None) -> list[dict]:
    projection = {"_id": {"$toString": "$_id"},
                  "created_by": 1,
                  "condition": 1,
                  "initialQuantity": 1,
                  "currentQuantity": 1,
                  "type": 1,
                  "details":1
                  }
    #projection["_id"] = {"$toString": "$_id"}
    if (resource_id is None):
        query = {}
    else:
        if (ObjectId.is_valid(resource_id)):
            query = {"_id": ObjectId(resource_id)}
        else:
            raise ValueError(f"Resource id {resource_id} is invalid")

    resources_data = resources_collection.find(query, projection)

    if (resources_data.explain()["executionStats"]["nReturned"] == 0):
        if resource_id is None:
            resource_id = ""
        raise ValueError(f"Resource {resource_id} does not exist")
    result_list = create_json_for_successful_data_fetch(resources_data, "resources")
    return result_list
    
def update_resource(resource_id: str, resource: Resource) -> Resource:
    # Fetch the existing resource
    existing_resource = resources_collection.find_one({"_id": ObjectId(resource_id)})
    if existing_resource:
        if 'details' in resource.dict(exclude_none=True) and 'details' in existing_resource:
            resource.details = {**existing_resource['details'], **resource.dict(exclude_none=True)['details']}

        update_data = {k: v for k, v in resource.dict(exclude_none=True).items()}

        resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": update_data})

        updated_resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)})
        return Resource(**updated_resource_data)
    else:
        raise ValueError(f"Resource id {resource_id} not found")



def delete_resource(resource_id: str):
    try:
        d = resources_collection.delete_one({"_id": ObjectId(resource_id)})
        if d.deleted_count == 0:
            raise
    except:
        raise ValueError(f"Resource {resource_id} cannot be deleted")
