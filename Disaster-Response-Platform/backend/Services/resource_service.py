from Models.resource_model import Resource, ConditionEnum
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from pymongo import ASCENDING, DESCENDING
from Services.build_API_returns import *
from typing import Optional
from datetime import datetime, timedelta

# Get the resources collection using the MongoDB class
resources_collection = MongoDB.get_collection('resources')
def validate_coordinates(x=None, y=None):
    if (x is not None and ((x < -90 or x > 90)) or (y is not None and (y < -180 or y > 180))):
        raise ValueError("X coordinates should be within -90 and 90, and y coordinates within -180 and 180")

def validate_quantities(initial_quantity=None, currentQuantity=None):
    if (initial_quantity is not None and (initial_quantity <= 0)) or (currentQuantity is not None and (currentQuantity <= 0)):
        raise ValueError("Quantities can't be less than or equal to 0")   

r_id=0
def create_resource(resource: Resource) -> str:
    global r_id
    # Manual validation for required fields during creation
    if not all([resource.created_by, resource.condition,
                resource.initialQuantity, resource.currentQuantity,
                resource.type, resource.details, resource.x is not None, resource.y is not None]):
        raise ValueError("All fields are mandatory for creation.")

    validate_coordinates(resource.x, resource.y)
    validate_quantities(resource.initialQuantity, resource.currentQuantity)
        
    
    if(resource.recurrence_rate!= None):
        if not all([resource.recurrence_deadline, resource.occur_at]):
            raise ValueError("Recurrence fields need to be entered")
        resource.recurrence_id=r_id
        resource.recurrence_rate = resource.recurrence_rate.value
        insert_result = resources_collection.insert_one(resource.dict())
 
        insert_ids=create_recurrent_resources(resource)

        r_id+=1
    else:
        insert_result = resources_collection.insert_one(resource.dict())
 

    #check the result to change from
    if insert_result.inserted_id:
        return "{\"resources\":[{\"_id\":" + f"\"{insert_result.inserted_id}\"" + "}]}"
    else:
        raise ValueError("Resource could not be created")
    
def create_recurrent_resources(resource: Resource):

    current_date = resource.occur_at
    current_date += timedelta(days=resource.recurrence_rate)
    insert_ids=[]

    def normalize_date(dt):
        return datetime(dt.year, dt.month, dt.day)
    while normalize_date(current_date) <= normalize_date(resource.recurrence_deadline):
        resource.occur_at=current_date
        resource.recurrence_id=r_id
        insert_result = resources_collection.insert_one(resource.dict())
        print("resource added ", insert_result, r_id, resource.occur_at)
        if(insert_result):
            insert_ids.append(insert_result)
            current_date += timedelta(days=resource.recurrence_rate)
        else:
            raise ValueError("Resource could not be created")
    return insert_ids

def get_resource_by_id(resource_id: str) -> list[dict]:
    return get_resources(resource_id)

def get_resources(
    resource_id: str = None,
    active: Optional[bool] = None,
    types: list = None, 
    subtypes: list = None, 
    x: float = None,
    y: float = None,
    distance_max: float = None,
    sort_by: str = 'created_at',
    order: Optional[str] = 'asc'
) -> list[dict]:
    projection = {"_id": {"$toString": "$_id"},
                  "created_by": 1,
                  "description": 1,
                  "condition": 1,
                  "initialQuantity": 1,
                  "currentQuantity": 1,
                  "type": 1,
                  "details":1,
                  "recurrence_id": 1,
                  "recurrence_rate": 1,
                  "recurrence_deadline": 1,
                  "x":1,
                  "y":1,
                  "active": 1,
                  "occur_at": 1,
                  "created_at":1,
                  "last_updated_at":1,
                  "upvote":1,
                  "downvote":1
                  }
    
    sort_order = ASCENDING if order == 'asc' else DESCENDING
    query = {}
    if resource_id:
        if ObjectId.is_valid(resource_id):
            query['_id'] = ObjectId(resource_id)
        else:
            raise ValueError(f"Resource id {resource_id} is invalid")
    else:
        # Apply general filters only when no specific resource ID is provided
        
        if active is not None:
            query['active'] = active    
        if types:
            query['type'] = {'$in': types}
        if subtypes:
            query['details.subtype'] = {'$in': subtypes}
    
    # Apply the query and sort order to the database call
    resources_cursor = resources_collection.find(query, projection).sort(sort_by, sort_order)
    resources_data = list(resources_cursor)  # Convert cursor to list

    # Filter by distance if necessary
    if x is not None and y is not None and distance_max is not None:
        resources_data = [res for res in resources_data if ((res['x'] - x) ** 2 + (res['y'] - y) ** 2) ** 0.5 <= distance_max]
    
    # Format the resource data
    formatted_resources_data = []
    for resource in resources_data:
        if 'created_at' in resource:
            resource['created_at'] = resource['created_at'].strftime('%Y-%m-%d %H:%M:%S')
        if 'last_updated_at' in resource:
            resource['last_updated_at'] = resource['last_updated_at'].strftime('%Y-%m-%d %H:%M:%S')
        formatted_resources_data.append(resource)

    # Generate the result list
    result_list = create_json_for_successful_data_fetch(resources_data, "resources")
    return result_list
    
def update_resource(resource_id: str, resource: Resource) -> Resource:
    # Fetch the existing resource
    existing_resource = resources_collection.find_one({"_id": ObjectId(resource_id)})
    if existing_resource:
        
        # Validate coordinates
        validate_coordinates(resource.x, resource.y)

        # Validate quantities
        validate_quantities(resource.initialQuantity, resource.currentQuantity)
        if 'details' in resource.dict(exclude_none=True) and 'details' in existing_resource:
            resource.details = {**existing_resource['details'], **resource.dict(exclude_none=True)['details']}

        update_data = {k: v for k, v in resource.dict(exclude_none=True).items()}

         # Retain the original 'created_at' field from the existing resource
        if 'created_at' in existing_resource:
            update_data['created_at'] = existing_resource['created_at']

        # Set 'last_updated_at' to the current time
        update_data['last_updated_at'] = datetime.now() + timedelta(hours=3)

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
    result = resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": {"initialQuantity": quantity, "last_updated_at": datetime.now() + timedelta(hours=3)}})
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
    result = resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": {"currentQuantity": quantity, "last_updated_at": datetime.now() + timedelta(hours=3)}})
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
    result = resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set": {"condition": condition.value, "last_updated_at": datetime.now() + timedelta(hours=3)}})
    if result.matched_count == 0:
        raise ValueError(f"Resource id {resource_id} not found")
    return True

def get_condition(resource_id: str) -> ConditionEnum:
    resource_data = resources_collection.find_one({"_id": ObjectId(resource_id)}, {"condition": 1})
    if resource_data:
        return ConditionEnum(resource_data["condition"])
    else:
        raise ValueError(f"Resource id {resource_id} not found")
