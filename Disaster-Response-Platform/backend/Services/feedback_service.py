from Models.feedback_model import Feedback, EntityTypeEnum
from Models.user_model import UserRole,UserInfo
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *
from Services.user_roles_service import *
from Services.authentication_service import *

feedback_collection = MongoDB.get_collection('feedback')

def vote(entityType:str, entityID:str, username:str, user_role:str,voteType:str) -> bool:
    existing_vote = feedback_collection.find_one({"entityType": entityType, "entityID": entityID, "username": username})   
    if existing_vote and existing_vote['vote']== voteType:
        raise ValueError(f"Current user already voted")
    elif existing_vote:
        result = feedback_collection.update_one({"entityType": entityType, "entityID": entityID, "username": username}, {"$set": {"vote": voteType}})
        if voteType == "upvote":
            if user_role== "ADMIN":
               set_upvote(entityType, entityID, 100) 
               set_downvote(entityType, entityID, -100)
            elif user_role== "CREDIBLE":
               set_upvote(entityType, entityID, 10) 
               set_downvote(entityType, entityID, -10) 
            elif user_role== "AUTHENTICATED" or user_role=="ROLE_BASED":    
                set_upvote(entityType, entityID, 1)
                set_downvote(entityType, entityID, -1)
            
            # set_upvote(entityType, entityID, 1)
            # set_downvote(entityType, entityID, -1)
        else:
            if user_role== "ADMIN":
               set_upvote(entityType, entityID, -100) 
               set_downvote(entityType, entityID, 100)
            elif user_role== "CREDIBLE":
               set_upvote(entityType, entityID, -10) 
               set_downvote(entityType, entityID, 10) 
            elif user_role== "AUTHENTICATED" or user_role=="ROLE_BASED":    
                set_upvote(entityType, entityID, -1)
                set_downvote(entityType, entityID,1)
                
            # set_upvote(entityType, entityID, -1)
            # set_downvote(entityType, entityID, 1)
    else:
        insert_result = feedback_collection.insert_one({"entityType":entityType, "entityID":entityID, "username": username, "vote": voteType})        
        if voteType=="upvote":
            if user_role== "ADMIN":
               set_upvote(entityType, entityID, 100) 
            elif user_role== "CREDIBLE":
               set_upvote(entityType, entityID, 10)  
            elif user_role== "AUTHENTICATED" or user_role=="ROLE_BASED":  
                set_upvote(entityType, entityID, 1)
             
        else:
            if user_role== "ADMIN":
               set_downvote(entityType, entityID, 100) 
            elif user_role== "CREDIBLE":
               set_downvote(entityType, entityID, 10)  
            elif user_role== "AUTHENTICATED" or user_role=="ROLE_BASED":  
                set_downvote(entityType, entityID, 1)
            

def unvote(entityType:str, entityID:str, username:str,user_role:str) -> bool:
    existing_vote = feedback_collection.find_one({"entityType": entityType, "entityID": entityID, "username": username})   
    if existing_vote:
        result = feedback_collection.delete_one({"entityType": entityType, "entityID": entityID, "username": username})
        if existing_vote['vote'] == "upvote":
            if user_role== "ADMIN":
               set_upvote(entityType, entityID, -100) 
            elif user_role== "CREDIBLE":
               set_upvote(entityType, entityID, -10)  
            elif user_role== "AUTHENTICATED" or user_role=="ROLE_BASED":  
                set_upvote(entityType, entityID, -1)
            
            # set_upvote(entityType, entityID, -1)
        else:
            if user_role== "ADMIN":
               set_downvote(entityType, entityID, -100) 
            elif user_role== "CREDIBLE":
               set_downvote(entityType, entityID, -10)  
            elif user_role== "AUTHENTICATED" or user_role=="ROLE_BASED":  
                set_downvote(entityType, entityID, -1)
            #set_downvote(entityType, entityID, -1)
    else:
        raise ValueError(f"Current user has not voted yet")
        
        
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


# Check the feedback status of the current user, entity pair.
def check_feeedback(entityType:str, entityID:str, current_user:str):
    existing_vote = feedback_collection.find_one({"entityType": entityType, "entityID": entityID, "username": current_user})
    if existing_vote:
        if existing_vote['vote'] == "upvote":
            return "upvote"
        else:
            return "downvote"
    else:
        return "none"