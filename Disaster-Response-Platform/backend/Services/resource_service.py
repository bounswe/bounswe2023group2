from Models.resource_model import Resource, ConditionEnum
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *
from datetime import datetime

# Get the resources collection using the MongoDB class
resources_collection = MongoDB.get_collection('resources')

def create_resource(resource: Resource) -> str:
    # Manual validation for required fields during creation
    if not all([resource.created_by, resource.condition,
                resource.initialQuantity, resource.currentQuantity,
                resource.type, resource.details, resource.x, resource.y]):
        raise ValueError("All fields are mandatory for creation.")
    insert_result = resources_collection.insert_one(resource.dict())
    #check the result to change from
    if insert_result.inserted_id:
        return "{\"resources\":[{\"_id\":" + f"\"{insert_result.inserted_id}\"" + "}]}"
    else:
        raise ValueError("Resource could not be created")

def get_resource_by_id(resource_id: str) -> list[dict]:
    return get_resources(resource_id)

def get_resources(resource_id:str = None) -> list[dict]:
    projection = {"_id": {"$toString": "$_id"},
                  "created_by": 1,
                  "description": 1,
                  "condition": 1,
                  "initialQuantity": 1,
                  "currentQuantity": 1,
                  "type": 1,
                  "details":1,
                  "x":1,
                  "y":1,
                  "created_at":1,
                  "last_updated_at":1
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

         # Retain the original 'created_at' field from the existing resource
        if 'created_at' in existing_resource:
            update_data['created_at'] = existing_resource['created_at']

        # Set 'last_updated_at' to the current time
        update_data['last_updated_at'] = datetime.now()

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
        return "{\"resources\":[{\"_id\":" + f"\"{resource_id}\"" + "}]}"
        # Returning the deleted id would be nice
    except:
        raise ValueError(f"Resource {resource_id} cannot be deleted")

def set_initial_quantity(resource_id: str, quantity: int) -> bool:
    result = resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": {"initialQuantity": quantity, "last_updated_at": datetime.now()}})
    if result.matched_count == 0:
        raise ValueError(f"Resource id {resource_id} not found")
    return True

def get_initial_quantity(resource_id: str) -> int:
    resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)}, {"initialQuantity": 1})
    if resource_data:
        return resource_data["initialQuantity"]
    else:
        raise ValueError(f"Resource id {resource_id} not found")

def set_current_quantity(resource_id: str, quantity: int) -> bool:
    result = resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": {"currentQuantity": quantity, "last_updated_at": datetime.now()}})
    if result.matched_count == 0:
        raise ValueError(f"Resource id {resource_id} not found")
    return True

def get_current_quantity(resource_id: str) -> int:
    resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)}, {"currentQuantity": 1})
    if resource_data:
        return resource_data["currentQuantity"]
    else:
        raise ValueError(f"Resource id {resource_id} not found")

def set_condition(resource_id: str, condition: ConditionEnum) -> bool:
    result = resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": {"condition": condition.value, "last_updated_at": datetime.now()}})
    if result.matched_count == 0:
        raise ValueError(f"Resource id {resource_id} not found")
    return True

def get_condition(resource_id: str) -> ConditionEnum:
    resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)}, {"condition": 1})
    if resource_data:
        return ConditionEnum(resource_data["condition"])
    else:
        raise ValueError(f"Resource id {resource_id} not found")
