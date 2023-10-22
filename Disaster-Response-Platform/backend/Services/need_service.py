from Models.need_model import Need
from Database.mongo import MongoDB
from bson.objectid import ObjectId

# Get the needs collection using the MongoDB class
needs_collection = MongoDB.get_collection('needs')

def create_need(need: Need) -> str:
    # Manual validation for required fields during creation
    if not all([need._id, need.created_by, need.urgency, 
                need.initialQuantity, need.unsuppliedQuantity, 
                need.type, need.details]):
        raise ValueError("All fields are mandatory for creation.")
    result = needs_collection.insert_one(need.dict())
    return str(result.inserted_id)

def get_need_by_id(need_id: str) -> Need:
    need_data = needs_collection.find_one({"_id": ObjectId(need_id)})
    if need_data:
        need_dict = {
            "_id": str(need_data["_id"]),
            "created_by": need_data["created_by"],
            "urgency": need_data["urgency"],
            "initialQuantity": need_data["initialQuantity"],
            "unsuppliedQuantity": need_data["unsuppliedQuantity"],
            "type": need_data["type"],
            "details": need_data["details"]
        }
        return need_dict
    return None

def get_all_needs() -> list[dict]:
    needs_data = needs_collection.find()
    
    return [{
        "_id": str(need_data["_id"]),
        "created_by": need_data["created_by"],
        "condition": need_data["condition"],
        "initialQuantity": need_data["initialQuantity"],
        "unsuppliedQuantity": need_data["unsuppliedQuantity"],
        "type": need_data["type"],
        "details": need_data["details"]
    } for need_data in needs_data]

    
def update_need(need_id: str, need: Need) -> Need:
    # Fetch the existing need
    existing_need = needs_collection.find_one({"_id": ObjectId(need_id)})

    # If details exist in the provided need and the database, merge them
    if 'details' in need.dict(exclude_none=True) and 'details' in existing_need:
        need.details = {**existing_need['details'], **need.dict(exclude_none=True)['details']}

    update_data = {k: v for k, v in need.dict(exclude_none=True).items()}

    needs_collection.update_one({"_id": ObjectId(need_id)}, {"$set": update_data})

    # updated_need_data = needs_collection.find_one({"_id": ObjectId(need_id)})
    return Need(update_data)



def delete_need(need_id: str):
    needs_collection.delete_one({"_id": ObjectId(need_id)})