from Models.resource_model import Resource
from Database.mongo import MongoDB
from bson.objectid import ObjectId

# Get the resources collection using the MongoDB class
resources_collection = MongoDB.get_collection('resources')

def create_resource(resource: Resource) -> str:
    result = resources_collection.insert_one(resource.dict())
    return str(result.inserted_id)

def get_resource_by_id(resource_id: str) -> Resource:
    resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)})
    if resource_data:
        return Resource(**resource_data)
    return None

def get_all_resources() -> list[Resource]:
    resources_data = resources_collection.find()
    return [Resource(**resource) for resource in resources_data]

def update_resource(resource_id: str, resource: Resource) -> Resource:
    resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": resource.dict()})
    updated_resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)})
    return Resource(**updated_resource_data)

def delete_resource(resource_id: str):
    resources_collection.delete_one({"_id": ObjectId(resource_id)})
