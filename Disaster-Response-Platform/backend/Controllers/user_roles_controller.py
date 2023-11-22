from fastapi import APIRouter, HTTPException, Response, Depends, status
from fastapi.security import OAuth2PasswordBearer,OAuth2PasswordRequestForm
from jose import JWTError, jwt
from datetime import datetime, timedelta
from Services import authentication_service
from fastapi.responses import JSONResponse
from http import HTTPStatus
import json
import config
from typing import Union
from Database.mongo import MongoDB
from Models.user_model import *
from Services.user_roles_service import *


router = APIRouter()
db = MongoDB.getInstance()

userDb = MongoDB.get_collection('authenticated_user')
roleRequestsDb = MongoDB.get_collection('role_verification_requests')


@router.post("/proficiency-request", responses={
    status.HTTP_201_CREATED: {"model": ProfReqSuccess},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
async def proficiency_request(prof_request : ProfRequest, response: Response, current_user: str = Depends(authentication_service.get_current_username)):

    try:
        result= create_proficiency_request(prof_request, current_user)
        response.status_code=status.HTTP_201_CREATED
        return result
    except ValueError as err:
        error= Error(ErrorMessage="Could not send proficiency verification request", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error

        return error
    
@router.get("/role", responses={
    status.HTTP_200_OK: {"model": UserRoleResponse},
    status.HTTP_400_BAD_REQUEST: {"model": Error}

})
async def get_user_role(response : Response, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    user_role_str = current_user.user_role.value   
    if user_role_str:
        response.status_code= HTTPStatus.OK
        return UserRoleResponse(user_role=user_role_str)
 
    else:
        error= Error(ErrorMessage="Could not find user role", ErrorDetail="User role is null")
        response.status_code= HTTPStatus.BAD_REQUEST
        #response.response_model= Error
        return error
    
@router.get("/proficiencies", responses={
    status.HTTP_200_OK: {"model": UserRoleResponse},
    status.HTTP_400_BAD_REQUEST: {"model": Error}

})
async def get_user_prof(response : Response, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    user_role_str = current_user.user_role.value   
    if user_role_str:
        response.status_code= HTTPStatus.OK
        return ProfResponse(user_role=user_role_str, proficiency= current_user.proficiency)
 
    else:
        error= Error(ErrorMessage="Could not find user role", ErrorDetail="User role is null")
        response.status_code= HTTPStatus.BAD_REQUEST
        #response.response_model= Error
        return error