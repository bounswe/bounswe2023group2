import datetime

import Services.utilities
from Models.event_model import Event, ActionRelations, NeedRelations
from Models.emergency_model import Emergency
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *


# Get the resources collection using the MongoDB class
emergencies_collection = MongoDB.get_collection('emergencies')
def create_event(emergency: Emergency) -> str:
    if not all([emergency.created_by_user, emergency.created_time,
                emergency.emergency_type, emergency.short_description, (emergency.is_active is not None)]):
        raise ValueError("Some mandatory fields missing.")
    emergency_dict = Services.utilities.correctDates(emergency)
    insert_result = emergencies_collection.insert_one(emergency_dict)

    if insert_result.inserted_id:
        return "{\"emergencies\":[{\"_id\":" + f"\"{insert_result.inserted_id}\"" + "}]}"
    else:
        raise ValueError("Emergency could not be created")

def get_emergency_by_id(emergency_id: str) -> list[dict]:
    return get_emergencies(emergency_id)

def get_emergencies(emergency_id:str = None) -> list[dict]:
    # projection = {"_id": {"$toString": "$_id"},
    #               "event_type": 1,
    #               "event_time": 1,
    #               "end_time": 1,
    #               "is_active":1,
    #               "center_location_x": 1,
    #               "center_location_y": 1,
    #               "max_distance_x": 1,
    #               "max_distance_y": 1,
    #               "created_time": 1,
    #               "created_by_user": 1,
    #               "last_confirmed_time": 1,
    #               "confirmed_by_user": 1,
    #               "short_description": 1,
    #               "note": 1
    # }

    if (emergency_id is None):
        query = {}
    else:
        if (ObjectId.is_valid(emergency_id)):
            query = {"_id": ObjectId(emergency_id)}
        else:
            raise ValueError(f"Emergency id {emergency_id} is invalid")

    emergencies_data = emergencies_collection.find(query)

    if (emergencies_data.explain()["executionStats"]["nReturned"] == 0):
        if emergency_id is None:
            raise ValueError(f"No emergency exists")
        else:
            raise ValueError(f"Emergency {emergency_id} does not exist")



    result_list = create_json_for_successful_data_fetch(emergencies_data, "emergencies")
    return result_list
    
def update_emergency(emergency_id: str, emergency: Emergency) -> list[dict]:
    # Fetch the existing resource
    existing_emengency = emergencies_collection.find_one({"_id": ObjectId(emergency_id)})
    new_event = emergency.dict()
    if existing_emengency:
        Services.utilities.set_Nones_to_old_values(new_event, existing_emengency)
        update_data = {k: v for k, v in new_event.items()}

        emergencies_collection.update_one({"_id": ObjectId(emergency_id)}, {"$set": update_data})

        updated_emergency_data = emergencies_collection.find({"_id": ObjectId(emergency_id)})
        result_list = create_json_for_successful_data_fetch(updated_emergency_data, "emergencies")
        return result_list
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

