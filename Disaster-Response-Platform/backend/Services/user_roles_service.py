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
    query = {"username": username}
    

    existing_user = userDb.find_one({"username": username})
def create_proficiency_request(prof_request: ProfRequest, username: str):
    # Update user role
    query = {"username": username}
    update_operation = {"$set": {"user_role": UserRole.ROLE_BASED.value}}
    userDb.update_one(query, update_operation)

    # Check if the user already has the specified proficiency
    existing_user = userDb.find_one({"username": username})
    if existing_user:
        # Check if the proficiency already exists
        if existing_user.get("proficiency", {}).get("proficiency") == prof_request.proficiency:
            # Update the details of the existing proficiency
            userDb.update_one(
                {"username": username},
                {"$set": {"proficiency.details": prof_request.details}}
            )
        else:
            # Set the new proficiency object
            userDb.update_one(
                {"username": username},
                {"$set": {"proficiency": {"proficiency": prof_request.proficiency, "details": prof_request.details}}},
                upsert=True
            )

        # Return success response
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
