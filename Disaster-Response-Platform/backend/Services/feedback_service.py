from Models.feedback_model import Feedback, EntityTypeEnum
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *


feedback_collection = MongoDB.get_collection('feedback')

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
        
        
def set_upvote(entity_type: EntityTypeEnum, entity_id: str, num: int) -> bool:
    collection = MongoDB.get_collection(entity_type.value)
    res = collection.update_one({"_id": ObjectId(entity_id)}, {"$inc": {"upvote": num}})   
    if res.matched_count == 0:
        raise ValueError(f"Entity type {entity_type.value} with entity id {entity_id} not found")
    return True
    

def set_downvote(entity_type: EntityTypeEnum, entity_id: str, num: int) -> bool:
    collection = MongoDB.get_collection(entity_type.value)
    res = collection.update_one({"_id": ObjectId(entity_id)}, {"$inc": {"downvote": num}})
    if res.matched_count == 0:
        raise ValueError(f"Entity type {entity_type.value} with entity id {entity_id} not found")
    return True

