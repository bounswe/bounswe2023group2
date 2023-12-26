import datetime

import Services.utilities
# from Models.event_model import Event, ActionRelations, NeedRelations
from Models.emergency_model import Emergency
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from pymongo import ASCENDING, DESCENDING
from Services.build_API_returns import *

from typing import Optional
from datetime import datetime, timedelta


# Get the emergencies collection using the MongoDB class
emergencies_collection = MongoDB.get_collection('emergencies')

def create_emergency(emergency: Emergency) -> str:
    if not all([emergency.emergency_type, emergency.description, emergency.location]):
        raise ValueError("Some mandatory fields missing : emergency_type, description, x, y,  location,")

    
    if emergency.created_by_user == "ANONYMOUS" and (not emergency.contact_name or not emergency.contact_number):
        raise ValueError("Contact name and number are required for anonymous users.")

    if emergency.created_at is None:
        emergency.created_at = datetime.now()
    
    Services.utilities.validate_coordinates(emergency.x, emergency.y)
    emergency_dict = Services.utilities.correctDates(emergency)
    insert_result = emergencies_collection.insert_one(emergency_dict)

    if insert_result.inserted_id:
        return "{\"emergencies\":[{\"_id\":" + f"\"{insert_result.inserted_id}\"" + "}]}"
    else:
        raise ValueError("Emergency could not be created")


def get_emergency_by_id(emergency_id: str) -> list[dict]:
    return get_emergencies(emergency_id)

def get_emergencies(
    emergency_id:str = None,
    is_active: Optional[bool] = None,
    is_verified: Optional[bool] = None,
    emergency_types: list = None, 
    x: float = None,
    y: float = None,
    distance_max: float = None,
    sort_by: str = 'created_at',
    order: Optional[str] = 'desc',
    contact_names: list = None,
    created_by_users: list = None
    ) -> list[dict]:
    projection = {"_id": {"$toString": "$_id"},
                  "created_by_user": 1,
                  "contact_name": 1,
                  "contact_number": 1,
                  "created_at":1,
                  "last_updated_at": 1,
                  "emergency_type": 1,
                  "description": 1,
                  "max_distance_y": 1,
                  "upvote": 1,
                  "downvote": 1,
                  "x": 1,
                  "y": 1,
                  "location": 1,
                  "is_active": 1,
                  "is_verified": 1,
                  "verification_note": 1
    }

    sort_order = ASCENDING if order == 'asc' else DESCENDING
    query = {}
    
    if emergency_id:
        if ObjectId.is_valid(emergency_id):
            query['_id'] = ObjectId(emergency_id)
        else:
            raise ValueError(f"Emergency id {emergency_id} is invalid")   
    else:
        # Apply general filters only when no specific emergency ID is provided
        if is_active is not None:
            query['is_active'] = is_active   
        if is_verified is not None:
            query['is_verified'] = is_verified   
         
        if emergency_types:
            query['emergency_type'] = {'$in': emergency_types}
        # if subtypes:
        #     query['details.subtype'] = {'$in': subtypes}
        if contact_names:
            query['contact_name'] = {'$in': contact_names}
        if created_by_users:
            query['created_by_user'] = {'$in': created_by_users}

    # Apply the query and sort order to the database call
    emergencies_cursor = emergencies_collection.find(query, projection).sort(sort_by, sort_order)
    emergencies_data = list(emergencies_cursor)  # Convert cursor to list

    # Filter by distance if necessary
    if x is not None and y is not None and distance_max is not None:
        emergencies_data = [res for res in emergencies_data if ((res['x'] - x) ** 2 + (res['y'] - y) ** 2) ** 0.5 <= distance_max]

        # Format the emergency data
    formatted_emergencies_data = []
    for emergency in emergencies_data:
        if 'created_at' in emergency:
            emergency['created_at'] = emergency['created_at'].strftime('%Y-%m-%d %H:%M:%S')
        formatted_emergencies_data.append(emergency)
  
    # if (emergencies_data.explain()["executionStats"]["nReturned"] == 0):
    #     if emergency_id is None:
    #         raise ValueError(f"No emergency exists")
    #     else:
    #         raise ValueError(f"Emergency {emergency_id} does not exist")

    result_list = create_json_for_successful_data_fetch(formatted_emergencies_data, "emergencies")
    return result_list
    
def update_emergency(emergency_id: str, emergency:Emergency) -> list[dict]:
    # Fetch the existing resource
    existing_emergency = emergencies_collection.find_one({"_id": ObjectId(emergency_id)})
    new_emergency = emergency.dict()
    if existing_emergency:
        Services.utilities.set_Nones_to_old_values(new_emergency, existing_emergency)
        update_data = {k: v for k, v in new_emergency.items()}

        emergencies_collection.update_one({"_id": ObjectId(emergency_id)}, {"$set": update_data})

        updated_emergency_data = emergencies_collection.find({"_id": ObjectId(emergency_id)})
        result_list = create_json_for_successful_data_fetch(updated_emergency_data, "emergencies")
        return result_list

        #return emergency(**updated_emergency_data)
    else:
        raise ValueError(f"Emergency id {emergency_id} not found")


def delete_emergency(emergency_id: str):
    try:
        d = emergencies_collection.delete_one({"_id": ObjectId(emergency_id)})
        if d.deleted_count == 0:
            raise
        return "{\"emergencies\":[{\"_id\":" + f"\"{emergency_id}\"" + "}]}"
        # Returning the deleted id would be nice
    except:
        raise ValueError(f"Emergency {emergency_id} cannot be deleted")