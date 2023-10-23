from Models.resource_model import Resource
from Database.mongo import MongoDB
from bson.objectid import ObjectId

# Get the resources collection using the MongoDB class
resources_collection = MongoDB.get_collection('resources')

def create_resource(resource: Resource) -> str:
    # Manual validation for required fields during creation
    if not all([resource._id, resource.created_by, resource.condition, 
                resource.initialQuantity, resource.unsuppliedQuantity, 
                resource.type, resource.details]):
        raise ValueError("All fields are mandatory for creation.")
    result = resources_collection.insert_one(resource.dict())
    return str(result.inserted_id)

def get_resource_by_id(resource_id: str) -> Resource:
    resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)})
    if resource_data:
        resource_dict = {
            "_id": str(resource_data["_id"]),
            "created_by": resource_data["created_by"],
            "condition": resource_data["condition"],
            "initialQuantity": resource_data["initialQuantity"],
            "currentQuantity": resource_data["currentQuantity"],
            "type": resource_data["type"],
            "details": resource_data["details"]
        }
        return resource_dict
    return None

def get_all_resources() -> list[dict]:
    resources_data = resources_collection.find()
    
    return [{
        "_id": str(resource_data["_id"]),
        "created_by": resource_data["created_by"],
        "condition": resource_data["condition"],
        "initialQuantity": resource_data["initialQuantity"],
        "currentQuantity": resource_data["currentQuantity"],
        "type": resource_data["type"],
        "details": resource_data["details"]
    } for resource_data in resources_data]

    
def update_resource(resource_id: str, resource: Resource) -> Resource:
    # Fetch the existing resource
    existing_resource = resources_collection.find_one({"_id": ObjectId(resource_id)})

    if existing_resource:
        # If details exist in the provided resource and the database, merge them
        if 'details' in resource.dict(exclude_none=True) and 'details' in existing_resource:
            resource.details = {**existing_resource['details'], **resource.dict(exclude_none=True)['details']}

        update_data = {k: v for k, v in resource.dict(exclude_none=True).items()}

        resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": update_data})

        updated_resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)})
        return Resource(**updated_resource_data)
    
    return None


def delete_resource(resource_id: str):
    resources_collection.delete_one({"_id": ObjectId(resource_id)})
