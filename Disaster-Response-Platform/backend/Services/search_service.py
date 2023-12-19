from Database.mongo import MongoDB
from fastapi.security import OAuth2PasswordBearer
from datetime import *
from fastapi import HTTPException, Depends, status
from jose import JWTError, jwt
from passlib.context import CryptContext
from Models.user_model import *
from Models.resource_model import *
from Models.need_model import *
from typing import Annotated
import config

#roleRequestsDb = MongoDB.get_collection('role_verification_requests')
userDb = MongoDB.get_collection('authenticated_user')
reource_collection = MongoDB.get_collection('resources')
need_collection = MongoDB.get_collection('needs')

def search_users(query: str)-> List[dict]:
    #if username is given
    cursor = userDb.find({
        "$or": [
            {"username": {"$regex": query, "$options": "i"}},
            {"email": {"$regex": query, "$options": "i"}},
            {"first_name": {"$regex": query, "$options": "i"}}
            # Add other fields if needed
        ]
    })
    results = [UserInfo(**document) for document in cursor]
    return results
    


def search_resources(query: str)-> List[dict]:
    #if username is given
    cursor = reource_collection.find({
        "$or": [
            {"created_by": {"$regex": query, "$options": "i"}},
            {"description": {"$regex": query, "$options": "i"}},
            {"type": {"$regex": query, "$options": "i"}},
            {"details.subtype": {"$regex": query, "$options": "i"}},
            {"details.type": {"$regex": query, "$options": "i"}}
            # Add other fields if needed
        ]
    })
    results = [Resource(**document) for document in cursor]
    return results
    

def search_needs(query: str)-> List[dict]:
    #if username is given
    cursor = need_collection.find({
        "$or": [
            {"created_by": {"$regex": query, "$options": "i"}},
            {"description": {"$regex": query, "$options": "i"}},
            {"type": {"$regex": query, "$options": "i"}},
            {"details.subtype": {"$regex": query, "$options": "i"}},
            {"details.type": {"$regex": query, "$options": "i"}}
            # Add other fields if needed
        ]
    })
    results = [Need(**document) for document in cursor]
    return results