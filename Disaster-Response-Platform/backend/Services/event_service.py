import datetime
from Models.event_model import Event, ActionRelations, NeedRelations
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *


# Get the resources collection using the MongoDB class
events_collection = MongoDB.get_collection('events')
time_format = "%Y-%m-%d %H:%M"

def eradicate_dates(event:Event):
    return_dict = event.dict()
    if return_dict["event_time"] is not None:
        return_dict["event_time"] = datetime.datetime.strptime(str(return_dict["event_time"]),  time_format)
    if return_dict["end_time"] is not None:
        return_dict["end_time"] = datetime.datetime.strptime(str(return_dict["end_time"]), time_format)
    if return_dict["created_time"] is not None:
        return_dict["created_time"] = datetime.datetime.strptime(str(return_dict["created_time"]), time_format)
    if return_dict["last_confirmed_time"] is not None:
        return_dict["last_confirmed_time"] = datetime.datetime.strptime(str(return_dict["last_confirmed_time"]), time_format)

    return return_dict

def set_Nones_to_old_values(dict_with_Nones:dict, dict_with_Olds:dict):
    for key, value in dict_with_Nones.items():
        if (value is None):
            dict_with_Nones[key] = dict_with_Olds[key]
def create_event(event: Event) -> str:
    # Manual validation for required fields during creation
    if not all([event.created_by_user, event.created_time,
                event.event_type, event.short_description, (event.is_active is not None)]):
        raise ValueError("Some mandatory fields missing.")
    event_dict = event.dict() #eradicate_dates(event)
    insert_result = events_collection.insert_one(event_dict)
    #check the result to change from
    if insert_result.inserted_id:
        return "{\"events\":[{\"_id\":" + f"\"{insert_result.inserted_id}\"" + "}]}"
    else:
        raise ValueError("Event could not be created")

def get_event_by_id(event_id: str) -> list[dict]:
    return get_events(event_id)

def get_events(event_id:str = None) -> list[dict]:
    projection = {"_id": {"$toString": "$_id"},
                  "event_type": 1,
                  "event_time": 1,
                  "end_time": 1,
                  "is_active":1,
                  "center_location_x": 1,
                  "center_location_y": 1,
                  "max_distance_x": 1,
                  "max_distance_y": 1,
                  "created_time": 1,
                  "created_by_user": 1,
                  "last_confirmed_time": 1,
                  "confirmed_by_user": 1,
                  "short_description": 1,
                  "note": 1
    }

    if (event_id is None):
        query = {}
    else:
        if (ObjectId.is_valid(event_id)):
            query = {"_id": ObjectId(event_id)}
        else:
            raise ValueError(f"Event id {event_id} is invalid")

    events_data = events_collection.find(query, projection)

    if (events_data.explain()["executionStats"]["nReturned"] == 0):
        if event_id is None:
            raise ValueError(f"No event exists")
        else:
            raise ValueError(f"Event {event_id} does not exist")

    result_list = create_json_for_successful_data_fetch(events_data, "events")
    return result_list
    
def update_event(event_id: str, event:Event) -> list[dict]:
    # Fetch the existing resource
    existing_event = events_collection.find_one({"_id": ObjectId(event_id)})
    new_event = event.dict()
    if existing_event:
        set_Nones_to_old_values(new_event, existing_event)
        update_data = {k: v for k, v in new_event.items()}

        events_collection.update_one({"_id": ObjectId(event_id)}, {"$set": update_data})

        updated_event_data = events_collection.find({"_id": ObjectId(event_id)})
        result_list = create_json_for_successful_data_fetch(updated_event_data, "events")
        return result_list

        #return Event(**updated_event_data)
    else:
        raise ValueError(f"Event id {event_id} not found")


def delete_event(event_id: str):
    try:
        d = events_collection.delete_one({"_id": ObjectId(event_id)})
        if d.deleted_count == 0:
            raise
        return "{\"events\":[{\"_id\":" + f"\"{event_id}\"" + "}]}"
        # Returning the deleted id would be nice
    except:
        raise ValueError(f"Event {event_id} cannot be deleted")

