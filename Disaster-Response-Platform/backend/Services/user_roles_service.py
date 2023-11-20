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
    result = userDb.update_one(
        {"username": username},
        {"$set": {"user_role": UserRole.ROLE_BASED.value, "proficiency": ProfRequest}},
    )
    if result.inserted_id:
        success_response = ProfReqSuccess(
            proficiency=dict(prof_request),
            inserted_id=str(result.inserted_id)  # Assuming 'result' is the inserted_id
        )
        return success_response

    # Check if the user was found and updated
    if result.modified_count == 0:
        raise HTTPException(status_code=404, detail="User not found")

    else:
        raise ValueError("Proficiency could not be added")
    