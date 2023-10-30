from Models.need_model import Need
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *


# Get the needs collection using the MongoDB class
needs_collection = MongoDB.get_collection('needs')

def create_need(need: Need) -> str:
    # Manual validation for required fields during creation
    if not all([need._id, need.created_by, need.urgency, 
                need.initialQuantity, need.unsuppliedQuantity, 
                need.type, need.details]):
        raise ValueError("All fields are mandatory for creation.")
    insert_result = needs_collection.insert_one(need.dict())
    if insert_result.inserted_id:
        return "{\"needs\":[" + json.dumps(dict(need)) + "], \"inserted_id\": " + f"\"{insert_result.inserted_id}\"" + "}"
    else:
        raise ValueError("Need could not be created")
    # return str(result.inserted_id)


def get_need_by_id(need_id: str) -> list[dict]:
    return get_needs(need_id)

# def get_need_by_id(need_id: str) -> Need:
#     need_data = needs_collection.find_one({"_id": ObjectId(need_id)})
#     if need_data:
#         need_dict = {
#             "_id": str(need_data["_id"]),
#             "created_by": need_data["created_by"],
#             "urgency": need_data["urgency"],
#             "initialQuantity": need_data["initialQuantity"],
#             "unsuppliedQuantity": need_data["unsuppliedQuantity"],
#             "type": need_data["type"],
#             "details": need_data["details"]
#         }
#         return need_dict
#     return None


def get_needs(need_id:str = None) -> list[dict]:
    projection = {
            "$toString": "$_id",
            "created_by": 1,
            "urgency": 1,
            "initialQuantity": 1,
            "unsuppliedQuantity": 1,
            "type": 1,
            "details": 1,
        }
    
    
    # {"_id": {"$toString": "$_id"},
    #               "created_by": 1,
    #               "condition": 1,
    #               "initialQuantity": 1,
    #               "currentQuantity": 1,
    #               "type": 1,
    #               "details":1
    #               }
    #projection["_id"] = {"$toString": "$_id"}
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


# def get_all_needs() -> list[dict]:
#     needs_data = needs_collection.find()
    
#     return [{
#         "_id": str(need_data["_id"]),
#         "created_by": need_data["created_by"],
#         "urgency": need_data["urgency"],
#         "initialQuantity": need_data["initialQuantity"],
#         "unsuppliedQuantity": need_data["unsuppliedQuantity"],
#         "type": need_data["type"],
#         "details": need_data["details"]
#     } for need_data in needs_data]

    
def update_need(need_id: str, need: Need) -> Need:
    # Fetch the existing need
    existing_need = needs_collection.find_one({"_id": ObjectId(need_id)})

    if existing_need:
        # If details exist in the provided need and the database, merge them
        if 'details' in need.dict(exclude_none=True) and 'details' in existing_need:
            need.details = {**existing_need['details'], **need.dict(exclude_none=True)['details']}

            update_data = {k: v for k, v in need.dict(exclude_none=True).items()}

            needs_collection.update_one({"_id": ObjectId(need_id)}, {"$set": update_data})

        updated_need_data = needs_collection.find_one({"_id": ObjectId(need_id)})
        return Need(**updated_need_data)
    else:
        raise ValueError(f"Need id {need_id} not found")


def delete_need(need_id: str):
    # needs_collection.delete_one({"_id": ObjectId(need_id)})
    
    try:
        d = needs_collection.delete_one({"_id": ObjectId(need_id)})
        if d.deleted_count == 0:
            raise
    except:
        raise ValueError(f"Need {need_id} cannot be deleted")    