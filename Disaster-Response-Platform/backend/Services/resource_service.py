from pymongo import MongoClient
from Models.resource_model import Resource

client = MongoClient('mongodb://localhost:27017/')
db = client['DisasterResponseLocal.Resources'] 

def create_resource(resource: Resource) -> str:
    result = resources_collection.insert_one(resource.dict())
    return str(result.inserted_id)

def get_resource_by_id(resource_id: str) -> Resource:
    resource_data = resources_collection.find_one({"_id": resource_id})
    if resource_data:
        return Resource(**resource_data)
    return None

def update_resource(resource_id: str, resource: Resource):
    resources_collection.update_one({"_id": resource_id}, {"$set": resource.dict()})

def delete_resource(resource_id: str):
    resources_collection.delete_one({"_id": resource_id})

