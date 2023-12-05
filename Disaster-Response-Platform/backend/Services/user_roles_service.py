from Database.mongo import MongoDB
from fastapi.security import OAuth2PasswordBearer
from datetime import *
from fastapi import HTTPException, Depends, status
from jose import JWTError, jwt
from passlib.context import CryptContext
from Models.user_model import *
from typing import Annotated
import config

#roleRequestsDb = MongoDB.get_collection('role_verification_requests')
userDb = MongoDB.get_collection('authenticated_user')

def create_proficiency_request(prof_request : ProfRequest, username: str):

    #old implementation which adds the request to the database to be reviewed by admin
    # insert_result = roleRequestsDb.insert_one(prof_request.dict())
    
    # if insert_result.inserted_id:
    #     success_response = ProfReqSuccess(
    #         proficiency=dict(prof_request),
    #         inserted_id=str(insert_result.inserted_id)  # Assuming 'result' is the inserted_id
    #     )
    #     return success_response
    query = {"username": username}
    update_operation = {"$set": {"user_role": UserRole.ROLE_BASED.value}}
    result= userDb.update_one(query, update_operation)

    existing_user = userDb.find_one({"username": username, "proficiency": {"$elemMatch": {"proficiency": prof_request.proficiency.value}}})
    if existing_user:
        userDb.update_one(
            {"username": username, "proficiency.proficiency": prof_request.proficiency.value},
            {"$set": {"proficiency.$.details": prof_request.details}}
        )
        success_response = ProfReqSuccess(
            proficiency=prof_request.proficiency
        )
        return success_response
    if existing_user:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="This proficiency already exists for the user.",
        )
    result = userDb.update_one(
        {"username": username},
        {"$push": {"proficiency": {"$each": [prof_request.dict()]}}},
        upsert=True,
    )
    if result.modified_count > 0 :
        success_response = ProfReqSuccess(
            proficiency=prof_request.proficiency
        )
        return success_response
    else:
        raise ValueError("Proficiency could not be added")
  
        
def get_user_role_service(username: str):
    existing_user = userDb.find_one({"username": username})
    if existing_user:
        return existing_user['user_role']
    else:
        raise ValueError("There is no such user")
