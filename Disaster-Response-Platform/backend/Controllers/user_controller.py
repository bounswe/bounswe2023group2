from fastapi import APIRouter, HTTPException, Response, Depends, status
from fastapi.security import OAuth2PasswordBearer,OAuth2PasswordRequestForm
from jose import JWTError, jwt
from typing import Optional
from datetime import datetime, timedelta
from Services import authentication_service
from fastapi.responses import JSONResponse
from http import HTTPStatus
import json
import config
from typing import Union
from Database.mongo import MongoDB
from Models.user_model import *
from Services.build_API_returns import create_json_for_error

router = APIRouter()
db = MongoDB.getInstance()
# Secret key to sign and verify the JWT token
SECRET_KEY = config.SECRET_KEY
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 1200
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")
userDb = MongoDB.get_collection('authenticated_user')


@router.post("/signup", responses={
    status.HTTP_201_CREATED: {"model": SignUpSuccess},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
async def signup(currentUser : CreateUserRequest, response: Response):
    try:
        result= authentication_service.create_user(currentUser)
        response.status_code=status.HTTP_201_CREATED
        return result
    except ValueError as err:
        error= Error(ErrorMessage="Signup failed", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error

        return error
   

@router.put("/update-user", responses={
    status.HTTP_200_OK: {"model": UserInfo},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
async def update_user_info(updated_user: UpdateUserRequest, response:Response,username: str = Depends(authentication_service.get_current_username)):
    try:
        result= authentication_service.update_user(username=username, updated_user=updated_user)
        return result
    except ValueError as err:
        error= Error(ErrorMessage="User update error", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error

        return error
   
        
# Login route
@router.post("/login", responses={
    status.HTTP_200_OK: {"model": Token},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}

})
async def login_for_access_token(user: LoginUserRequest, response:Response): ##douıble check here
    user = authentication_service.authenticate_user(user.username_or_email_or_phone, user.password)
    if not user:
        error= Error(ErrorMessage="Login failed", ErrorDetail= "Incorrect username or password")
        response.status_code= HTTPStatus.UNAUTHORIZED
        return error
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    login_response = authentication_service.create_jwt_token(data={"sub": user.username}, expires_delta=access_token_expires)
    return login_response


@router.post("/refresh-token", response_model=Token)
async def refresh_access_token(current_user: str  = Depends(authentication_service.get_current_username)):
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    login_response = authentication_service.create_jwt_token(
        data={"sub": current_user}, expires_delta=access_token_expires
    )
    return login_response

# Protected route
@router.get("/protected")
async def protected_route(current_user: str = Depends(authentication_service.get_current_user)):
    return {"message": f"Welcome, {current_user}!"}

@router.get("/me", responses={
    status.HTTP_200_OK: {"model": UserInfo},
    status.HTTP_400_BAD_REQUEST: {"model": Error},
    status.HTTP_500_INTERNAL_SERVER_ERROR: {"model": Error}
})
async def get_self_info(response:Response,user: UserProfile = Depends(authentication_service.get_current_user)):
    if not user:
        error= Error(ErrorMessage="Get user info failed", ErrorDetail= "Unexpected error")
        response.status_code= HTTPStatus.INTERNAL_SERVER_ERROR
        return error
    user_info= UserInfo(username=user.username,
                        email=user.email,
                        first_name=user.first_name,
                        last_name=user.last_name,
                        phone_number=user.phone_number,
                        is_email_verified=user.is_email_verified,
                        private_account=user.private_account)

    return user_info
  

# geT another users info
@router.get("/{username}", responses={
    status.HTTP_200_OK: {"model": UserInfo},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_500_INTERNAL_SERVER_ERROR: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})
async def get_user_info(response:Response,username: str, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    user = authentication_service.get_user(username)
    if not user:
        error= Error(ErrorMessage="Get user info failed", ErrorDetail= "User not found")
        response.status_code= status.HTTP_404_NOT_FOUND
        return error
      
    #if it is another user, the account
    if current_user.username != username and user.private_account:
        error= Error(ErrorMessage="Get user info failed", ErrorDetail= "User's information is private")
        response.status_code= status.HTTP_403_FORBIDDEN
        return error
    

    # Users can access their own information, return it here 
    user_info= UserInfo(username=user.username,
                        email=user.email,
                        first_name=user.first_name,
                        last_name=user.last_name,
                        phone_number=user.phone_number,
                        is_email_verified=user.is_email_verified,
                        private_account=user.private_account)

    return user_info


@router.put("/verify")
def verify_user(response:Response,user: UserUsername, current_user: UserProfile = Depends(authentication_service.get_current_admin_user)):
    try:
        verified = authentication_service.verify_user(user.username)
        response.status_code = HTTPStatus.OK
        return {f'{user.username} is verified'}    
    except ValueError as err:
        err_json = create_json_for_error("Verification error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.put("/unverify")
def unverify_user(response:Response,user: UserUsername, current_user: UserProfile = Depends(authentication_service.get_current_admin_user)):
    try:
        unverified = authentication_service.unverify_user(user.username)
        response.status_code = HTTPStatus.OK
        return {f'{user.username} is unverified'}   
    except ValueError as err:
        err_json = create_json_for_error("Unverification error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.put("/unauthorize")
def unauthorize_user(response:Response,user: UserUsername, current_user: UserProfile = Depends(authentication_service.get_current_admin_user)):
    try:
        unverified = authentication_service.unauthorize_user(user.username)
        response.status_code = HTTPStatus.OK
        return {f'{user.username} is unauthorized'}   
    except ValueError as err:
        err_json = create_json_for_error("Unauthorization error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
