from Models.feedback_model import Feedback
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *


# Get the needs collection using the MongoDB class
feedback_collection = MongoDB.get_collection('feedback')
needs_collection = MongoDB.get_collection('needs')
resources_collection = MongoDB.get_collection('resources')
events_collection = MongoDB.get_collection('events')
actions_collection = MongoDB.get_collection('actions')

def vote(entityType:str, entityID:str, current_user:str, voteType:str) -> bool:
    existing_vote = feedback_collection.find_one({"entityType": entityType, "entityID": entityID})   
    if existing_vote and existing_vote['vote']== voteType:
        raise ValueError(f"Current user already voted")
    elif existing_vote:
        result = feedback_collection.update_one({"entityType": entityType, "entityID": entityID}, {"$set": {"vote": voteType}})
        if voteType == "upvote":
            set_upvote(entityType, entityID, 1)
            set_downvote(entityType, entityID, -1)
        else:
            set_upvote(entityType, entityID, -1)
            set_downvote(entityType, entityID, 1)
    else:
        insert_result = feedback_collection.insert_one({"entityType":entityType, "entityID":entityID, "username": current_user, "vote": voteType})        
        if voteType=="upvote":
            set_upvote(entityType, entityID, 1)
        else:
            set_downvote(entityType, entityID, 1)
        

def get_entity_type_collection(entity_type: str):
    if entity_type == "need":
        collection = needs_collection
    elif entity_type == "resource":
        collection = resources_collection
    elif entity_type == "event":
        collection = events_collection
    elif entity_type == "action":
        collection = actions_collection
    return collection
        
        
def set_upvote(entity_type: str, entity_id: str, num: int) -> bool:
    collection = get_entity_type_collection(entity_type)
    res = collection.update_one({"_id": ObjectId(entity_id)}, {"$inc": {"upvote": num}})   
    if res.matched_count == 0:
        raise ValueError(f"Entity type {entity_type} with entity id {entity_id} not found")
    return True
    

def set_downvote(entity_type: str, entity_id: str, num: int) -> bool:
    collection = get_entity_type_collection(entity_type)
    res = collection.update_one({"_id": ObjectId(entity_id)}, {"$inc": {"downvote": num}})
    if res.matched_count == 0:
        raise ValueError(f"Entity type {entity_type} with entity id {entity_id} not found")
    return True

