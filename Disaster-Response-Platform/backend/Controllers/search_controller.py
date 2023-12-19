from fastapi import APIRouter, HTTPException, Response, Depends, status
from fastapi.security import OAuth2PasswordBearer,OAuth2PasswordRequestForm
from jose import JWTError, jwt
from typing import Optional
from datetime import datetime, timedelta
from Services import search_service, authentication_service
from fastapi.responses import JSONResponse
from http import HTTPStatus
import json
import config
from typing import Union
from Database.mongo import MongoDB
from Models.user_model import *
from Models.search_model import *
from Services.build_API_returns import create_json_for_error

router = APIRouter()
db = MongoDB.getInstance()
userDb = MongoDB.get_collection('authenticated_user')

@router.get("/users/{query}", responses={
    status.HTTP_200_OK: {"model": SearchResults},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})
async def search_users(response:Response,query: str, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    userList = search_service.search_users(query)
    

    return  userList
