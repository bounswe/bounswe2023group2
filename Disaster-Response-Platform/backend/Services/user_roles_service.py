from Database.mongo import MongoDB
from fastapi.security import OAuth2PasswordBearer
from datetime import *
from fastapi import HTTPException, Depends, status
from jose import JWTError, jwt
from passlib.context import CryptContext
from Models.user_model import *
from typing import Annotated
import config

roleRequestsDb = MongoDB.get_collection('role_verification_requests')


def create_proficiency_request(prof_request : ProfRequest):

    
    insert_result = roleRequestsDb.insert_one(prof_request.dict())
    
    if insert_result.inserted_id:
        success_response = ProfReqSuccess(
            proficiency=dict(prof_request),
            inserted_id=str(insert_result.inserted_id)  # Assuming 'result' is the inserted_id
        )
        return success_response
    else:
        raise ValueError("Proficiency could not be added")