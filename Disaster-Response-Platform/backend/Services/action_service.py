from Models.resource_model import ConditionEnum
from Models.action_model import *
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *


# Get the resources collection using the MongoDB class
actions_collection = MongoDB.get_collection('actions')


def create_action(action: Action) -> str:
    # Manual validation for required fields during creation
    #can there be actions without a resource
    if not all([action.created_by,
                action.details, action.x, action.y, action.needs, action.resources]):
        raise ValueError("All fields are mandatory for creation.")
    insert_result = actions_collection.insert_one(action.dict())
    #check the result to change from
    if insert_result.inserted_id:
        ActionSuccess(action_id= insert_result.inserted_id)
    else:
        raise ValueError("Action could not be created")