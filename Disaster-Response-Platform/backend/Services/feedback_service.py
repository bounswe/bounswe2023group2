from Models.need_model import Feedback
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *


# Get the needs collection using the MongoDB class
feedback_collection = MongoDB.get_collection('feedback')
needs_collection = MongoDB.get_collection('needs')
resources_collection = MongoDB.get_collection('resources')
events_collection = MongoDB.get_collection('events')
actions_collection = MongoDB.get_collection('actions')

def vote(entityType:str, entityID:str, voteType:str, current_user:str, voteType:str) -> bool:
    existing_vote = feedback_collection.find({"entityType": entityType, "entityID": entityID})   
    if existing_vote == None:
        insert_result = feedback_collection.insert_one({"entityType":entityType, "entityID":entityID, "username": current_user, "vote": voteType})
    elif existing_vote.vote == voteType:
        raise ValueError(f"Current user already voted")
    else:
        result = feedback_collection.update_one({"entityType": entityType, "entityID": entityID}, {"$set": {"vote": voteType}})
      

def set_upvote(entity_type: str, entity_id: str) -> bool:
    if entity_type == "need":
        collection = needs_collection
    elif entity_type == "resource":
        collection = resources_collection
    elif entity_type == "event":
        collection = events_collection
    elif entity_type == "action":
        collection = actions_collection
    
    res = collection.update_one({"entityType": entityType, "entityID": entityID}, {"$inc": {"upvote": 1}})   
         
    if result.matched_count == 0:
        raise ValueError(f"Need id {need_id} not found")
    return True
    

def set_downvote(need_id: str) -> bool:
    result = needs_collection.update_one({"_id": ObjectId(need_id)}, {"$inc": {"downvote": 1}})
    if result.matched_count == 0:
        raise ValueError(f"Need id {need_id} not found")
    return True
    
