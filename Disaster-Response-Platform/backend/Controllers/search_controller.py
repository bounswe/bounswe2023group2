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
import requests
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



@router.get("/actions/{query}", responses={
    status.HTTP_200_OK: {"model": SearchResults},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})
async def search_actions(response:Response,query: str, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    try:
        actionList = search_service.search_actions(query)
        return  actionList
    except ValueError as err:
        error= Error(ErrorMessage="Action Search Failed", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error

        return error

    


@router.get("/events/{query}", responses={
    status.HTTP_200_OK: {"model": SearchResults},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})
async def search_events(response:Response,query: str, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    userList = search_service.search_events(query)
    

    return  userList


@router.get("/needs/{query}", responses={
    status.HTTP_200_OK: {"model": SearchResults},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})
async def search_events(response:Response,query: str, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    userList = search_service.search_needs(query)
    

    return  userList


@router.get("/resources/{query}", responses={
    status.HTTP_200_OK: {"model": SearchResults},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})
async def search_resources(response:Response,query: str, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    userList = search_service.search_resources(query)
    

    return  userList