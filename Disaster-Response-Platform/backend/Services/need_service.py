from Models.need_model import Need
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from Services.build_API_returns import *
from pymongo import ASCENDING, DESCENDING
from typing import Optional
from datetime import datetime, timedelta

# Get the needs collection using the MongoDB class
needs_collection = MongoDB.get_collection('needs')
r_id=0
def create_need(need: Need) -> str:
    global r_id
    # Manual validation for required fields during creation
    if not all([need.created_by, need.urgency, 
                need.initialQuantity, need.unsuppliedQuantity, 
                need.type, need.details]):
        raise ValueError("All fields are mandatory for creation.")
    
    if(need.recurrence_rate!= None):
        if not all([need.recurrence_deadline, need.occur_at]):
            raise ValueError("Recurrence fields need to be entered")
        need.recurrence_id=r_id
        need.recurrence_rate = need.recurrence_rate.value
        insert_result = needs_collection.insert_one(need.dict())
        print("need added ", insert_result, need.recurrence_id, need.occur_at)
        insert_ids=create_recurrent_needs(need)
        print(insert_ids)
        r_id+=1
    else:
        insert_result = needs_collection.insert_one(need.dict())
        print("need added ", insert_result, need.recurrence_id, need.occur_at)

    
    
    if insert_result.inserted_id:
        result = "{\"needs\":[{\"_id\":" + f"\"{insert_result.inserted_id}\""+"}]}"
        return result
    else:
        raise ValueError("Need could not be created")
    # return str(result.inserted_id)


def get_need_by_id(need_id: str) -> list[dict]:
    return get_needs(need_id)

def create_recurrent_needs(need:Need):

    current_date = need.occur_at
    current_date += timedelta(days=need.recurrence_rate)
    insert_ids=[]

    def normalize_date(dt):
        return datetime(dt.year, dt.month, dt.day)
    while normalize_date(current_date) <= normalize_date(need.recurrence_deadline):
        need.occur_at=current_date
        need.recurrence_id=r_id
        insert_result = needs_collection.insert_one(need.dict())
        print("need added ", insert_result, r_id, need.occur_at)
        if(insert_result):
            insert_ids.append(insert_result)
            current_date += timedelta(days=need.recurrence_rate)
        else:
            raise ValueError("Need could not be created")
    return insert_ids
    

def get_needs(
    need_id: str = None,
    active: Optional[bool] = None,
    types: list = None, 
    x: float = None,
    y: float = None,
    distance_max: float = None,
    subtypes: list = None,
    sort_by: str = 'created_at',
    order: Optional[str] = 'asc'
) -> list[dict]:
    projection = {
            "_id": {"$toString": "$_id"},
            "created_by": 1,
            "description": 1,
            "urgency": 1,
            "initialQuantity": 1,
            "unsuppliedQuantity": 1,
            "type": 1,
            "details": 1,
            "recurrence_id": 1,
            "recurrence_rate": 1,
            "recurrence_deadline": 1,
            "x": 1,
            "y": 1,
            "active": 1,
            "occur_at": 1,
            "created_at": 1,
            "last_updated_at": 1,
            "upvote": 1,
            "downvote": 1
            # Add other fields if necessary
        }
    
    sort_order = ASCENDING if order == 'asc' else DESCENDING
    query = {}
    
    if need_id:
        if ObjectId.is_valid(need_id):
            query = {"_id": ObjectId(need_id)}
        else:
            raise ValueError(f"Need id {need_id} is invalid")
    else:
        if active is not None:
            query['active'] = active
        if types:
            query['type'] = {'$in': types}
        if subtypes:
            query['details.subtype'] = {'$in': subtypes}

    needs_cursor = needs_collection.find(query, projection).sort(sort_by, sort_order)
    needs_data = list(needs_cursor)

    # Filter by distance if necessary
    if x is not None and y is not None and distance_max is not None:
        needs_data = [res for res in needs_data if ((res['x'] - x) ** 2 + (res['y'] - y) ** 2) ** 0.5 <= distance_max]
   
    # Formatting datetime fields
    formatted_needs_data = []
    for need in needs_data:
        if 'created_at' in need:
            need['created_at'] = need['created_at'].strftime('%Y-%m-%d %H:%M:%S')
        if 'last_updated_at' in need:
            need['last_updated_at'] = need['last_updated_at'].strftime('%Y-%m-%d %H:%M:%S')
        formatted_needs_data.append(need)

    result_list = create_json_for_successful_data_fetch(formatted_needs_data, "needs")
    return result_list

    
def update_need(need_id: str, need: Need) -> Need:
    # Fetch the existing need
    existing_need = needs_collection.find_one({"_id": ObjectId(need_id)})

    if existing_need:
        # If details exist in the provided need and the database, merge them
        if 'details' in need.dict(exclude_none=True) and 'details' in existing_need:
            need.details = {**existing_need['details'], **need.dict(exclude_none=True)['details']}

        update_data = {k: v for k, v in need.dict(exclude_none=True).items()}

        # Retain the original 'created_at' field from the existing need
        if 'created_at' in existing_need:
            update_data['created_at'] = existing_need['created_at']

        # Set 'last_updated_at' to the current time
        update_data['last_updated_at'] = datetime.now()

        needs_collection.update_one({"_id": ObjectId(need_id)}, {"$set": update_data})

        updated_need_data = needs_collection.find_one({"_id": ObjectId(need_id)})
        return Need(**updated_need_data)
    else:
        raise ValueError(f"Need id {need_id} not found")


def delete_need(need_id: str):
    
    try:
        d = needs_collection.delete_one({"_id": ObjectId(need_id)})
        if d.deleted_count == 0:
            raise
        return "{\"needs\":[{\"_id\":" + f"\"{need_id}\"" + "}]}"
    except:
        raise ValueError(f"Need {need_id} cannot be deleted")    
    
def set_initial_quantity(need_id: str, quantity: int) -> bool:
    result = needs_collection.update_one({"_id": ObjectId(need_id)}, {"$set": {"initialQuantity": quantity, "last_updated_at": datetime.now()}})
    if result.matched_count == 0:
        raise ValueError(f"Need id {need_id} not found")
    return True
    
def get_initial_quantity(need_id: str) -> int:
    need_data = needs_collection.find_one({"_id": ObjectId(need_id)})
    if need_data:
        return need_data["initialQuantity"]
    else:
        raise ValueError(f"Need id {need_id} not found")
    
def set_unsupplied_quantity(need_id: str, quantity: int) -> bool:
    result = needs_collection.update_one({"_id": ObjectId(need_id)}, {"$set": {"unsuppliedQuantity": quantity, "last_updated_at": datetime.now()}})
    if result.matched_count == 0:
        raise ValueError(f"Need id {need_id} not found")
    return True
    
def get_unsupplied_quantity(need_id: str) -> int:
    need_data = needs_collection.find_one({"_id": ObjectId(need_id)})
    if need_data:
        return need_data["unsuppliedQuantity"]
    else:
        raise ValueError(f"Need id {need_id} not found")
    
def set_urgency(need_id: str, urgency: int) -> bool:
    result = needs_collection.update_one({"_id": ObjectId(need_id)}, {"$set": {"urgency": urgency, "last_updated_at": datetime.now()}})
    if result.matched_count == 0:
        raise ValueError(f"Need id {need_id} not found")
    return True
    
def get_urgency(need_id: str) -> int:
    need_data = needs_collection.find_one({"_id": ObjectId(need_id)})
    if need_data:
        return need_data["urgency"]
    else:
        raise ValueError(f"Need id {need_id} not found")
