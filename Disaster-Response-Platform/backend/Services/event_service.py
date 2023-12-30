import datetime
from Models.event_model import Event, ActionRelations, NeedRelations
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *
import Services.utilities

from pymongo import ASCENDING, DESCENDING
from typing import Optional
import math

# Get the resources collection using the MongoDB class
events_collection = MongoDB.get_collection('events')

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

def get_events(event_id: str = None, types: list = None,
               is_active: Optional[bool] = None,
               x: float = None,
               y: float = None,
               distance_max: float = None,
               sort_by: str = 'created_at',
               order: Optional[str] = 'desc'
               ) -> list[dict]:
    projection = {
        "_id": {"$toString": "$_id"},
        "event_type": 1,
        "event_time": 1,
        "end_time": 1,
        "is_active": 1,
        "x": 1,
        "y": 1,
        "max_distance_x": 1,
        "max_distance_y": 1,
        "created_time": 1,
        "created_by_user": 1,
        "last_confirmed_time": 1,
        "confirmed_by_user": 1,
        "short_description": 1,
        "downvote": 1,
        "upvote": 1,
        "reliability": 1,
        "note": 1
    }
    sort_order = ASCENDING if order == 'asc' else DESCENDING
    query = {"_id":event_id}

    if event_id:
        if not (ObjectId.is_valid(event_id)):
            raise ValueError(f"Event id {event_id} is invalid")
        else:
            query = {"_id": ObjectId(event_id)}
    else:
        # Apply general filters only when no specific resource ID is provided
        query = {}
        if is_active is not None:
            query['is_active'] = is_active
        if types:
            query['event_type'] = {'$in': types}
    # Apply the query and sort order to the database call
    events_cursor = events_collection.find(query, projection).sort(sort_by, sort_order)
    events_data = list(events_cursor)  # Convert cursor to list

    # Filter by distance if necessary
    if x is not None and y is not None and distance_max is not None:
        events_data = [res for res in events_data if
                       ((res['x'] - x) ** 2 + (res['y'] - y) ** 2) ** 0.5 <= distance_max]

    for event in events_data:
        upvotes = event.get('upvote', 0)
        downvotes = event.get('downvote', 0)
        total_votes = upvotes + downvotes

        if total_votes > 0:
            z = 1.96  # 95% confidence interval
            phat = upvotes / total_votes
            event['reliability'] = (
                        phat + z ** 2 / (2 * total_votes) - z * math.sqrt(
                    (phat * (1 - phat) + z ** 2 / (4 * total_votes)) / total_votes)) / (1 + z ** 2 / total_votes)
        else:
            event['reliability'] = 0.5  # Default score for no votes

        # Calculate and sort by reliability score if requested
    if sort_by == 'reliability':
        events_data.sort(key=lambda x: x['reliability'], reverse=(order == 'desc'))

    # Formatting datetime fields
    formatted_events_data = []
    for event in events_data:
        if 'created_time' in event:
            event['created_time'] = event['created_time'].strftime('%Y-%m-%d %H:%M:%S')
        if 'last_confirmed_time' in event:
            if event['last_confirmed_time'] is not None:
                event['last_confirmed_time'] = event['last_confirmed_time'].strftime('%Y-%m-%d %H:%M:%S')
        formatted_events_data.append(event)

    # Generate the result list
    result_list = create_json_for_successful_data_fetch(formatted_events_data, "events")
    return result_list


# def get_events_X(event_id:str = None) -> list[dict]:
#     projection = {"_id": {"$toString": "$_id"},
#                   "event_type": 1,
#                   "event_time": 1,
#                   "end_time": 1,
#                   "is_active":1,
#                   "x": 1,
#                   "y": 1,
#                   "max_distance_x": 1,
#                   "max_distance_y": 1,
#                   "created_time": 1,
#                   "created_by_user": 1,
#                   "last_confirmed_time": 1,
#                   "confirmed_by_user": 1,
#                   "short_description": 1,
#                   "downvote": 1,
#                   "upvote": 1,
#                   "note": 1
#     }

#     if (event_id is None):
#         query = {}
#     else:
#         if (ObjectId.is_valid(event_id)):
#             query = {"_id": ObjectId(event_id)}
#         else:
#             raise ValueError(f"Event id {event_id} is invalid")

#     events_data = events_collection.find(query, projection)

#     if (events_data.explain()["executionStats"]["nReturned"] == 0):
#         if event_id is None:
#             raise ValueError(f"No event exists")
#         else:
#             raise ValueError(f"Event {event_id} does not exist")

#     result_list = create_json_for_successful_data_fetch(events_data, "events")
#     return result_list
    
def update_event(event_id: str, event:Event) -> list[dict]:
    # Fetch the existing resource
    existing_event = events_collection.find_one({"_id": ObjectId(event_id)})
    new_event = event.dict()
    if existing_event:
        Services.utilities.set_Nones_to_old_values(new_event, existing_event)
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

