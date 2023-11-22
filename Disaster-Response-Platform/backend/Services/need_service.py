from Models.need_model import Need
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *
from datetime import datetime

# Get the needs collection using the MongoDB class
needs_collection = MongoDB.get_collection('needs')

def validate_coordinates(x, y):
    if (x < -90 or x > 90) or (y < -180 or y > 180):
        raise ValueError("X coordinates should be within -90 and 90 and y coordinates within -180 and 180")

def validate_quantities(initial_quantity, unsupplied_quantity):
    if initial_quantity <= 0 or unsupplied_quantity <= 0:
        raise ValueError("Quantities can't be less than or equal to 0")

def create_need(need: Need) -> str:
    # Manual validation for required fields during creation
    if not all([need.created_by, need.urgency, 
                need.initialQuantity, need.unsuppliedQuantity, 
                need.type, need.details, need.x is not None, need.y is not None]):
        raise ValueError("All fields are mandatory for creation.")
    validate_coordinates(need.x, need.y)
    validate_quantities(need.initialQuantity, need.unsuppliedQuantity)
    if need.initialQuantity <= 0 or need.unsuppliedQuantity <= 0:
        raise ValueError("Quantities can't be less than or equal to 0")
    insert_result = needs_collection.insert_one(need.dict())
    if insert_result.inserted_id:
        result = "{\"needs\":[{\"_id\":" + f"\"{insert_result.inserted_id}\""+"}]}"
        return result
    else:
        raise ValueError("Need could not be created")
    # return str(result.inserted_id)


def get_need_by_id(need_id: str) -> list[dict]:
    return get_needs(need_id)



def get_needs(need_id:str = None) -> list[dict]:
    projection = {
            "_id": {"$toString": "$_id"},
            "created_by": 1,
            "urgency": 1,
            "initialQuantity": 1,
            "unsuppliedQuantity": 1,
            "type": 1,
            "details": 1,
            "x": 1,
            "y": 1,
            "created_at": 1,
            "last_updated_at": 1
        }
    
    if (need_id is None):
        query = {}
    else:
        if (ObjectId.is_valid(need_id)):
            query = {"_id": ObjectId(need_id)}
        else:
            raise ValueError(f"Need id {need_id} is invalid")

    needs_data = needs_collection.find(query, projection)

    if (needs_data.explain()["executionStats"]["nReturned"] == 0):
        if need_id is None:
            need_id = ""
        raise ValueError(f"Need {need_id} does not exist")
    result_list = create_json_for_successful_data_fetch(needs_data, "needs")
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
