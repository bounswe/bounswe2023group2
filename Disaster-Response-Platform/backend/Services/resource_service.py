from pymongo import MongoClient
from Models.resource_model import Resource

client = MongoClient('mongodb://localhost:27017/')
db = client['DisasterResponseLocal'] 
resources_collection = db['resources'] 

def create_resource(resource: Resource) -> str:
    # Insert the resource into the database and return its ID
    pass

def get_resource_by_id(resource_id: str) -> Resource:
    # Fetch the resource from the database using its ID
    pass

def update_resource(resource_id: str, resource: Resource):
    # Update the resource in the database
    pass

def delete_resource(resource_id: str):
    # Delete the resource from the database
    pass
