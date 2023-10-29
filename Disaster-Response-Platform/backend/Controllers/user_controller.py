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
ACCESS_TOKEN_EXPIRE_MINUTES = 120
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")
userDb = MongoDB.get_collection('authenticated_user')


@router.post("/signup", responses={
    status.HTTP_201_CREATED: {"model": SignUpSuccess},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
async def signup(currentUser : CreateUserRequest, response: Response):
    try:
        result= authentication_service.create_user(currentUser)

        return result
    except ValueError as err:
        error= Error(ErrorMessage="Signup failed", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error

        return error
   

@router.put("/update-user", status_code=200)
async def update_user_info(updated_user: UpdateUserRequest, response:Response,username: str = Depends(authentication_service.get_current_username)):
    try:
        result= authentication_service.update_user(username=username, updated_user=updated_user)
        return result
    except ValueError as err:
        err_json = create_json_for_error("User update error", str(err))
        response.status_code = HTTPStatus.BAD_REQUEST
        return json.loads(err_json)
   
        
# Login route
@router.post("/login", response_model=Token, status_code=200)
async def login_for_access_token(user: LoginUserRequest): ##douıble check here
    user = authentication_service.authenticate_user(user.username_or_email_or_phone, user.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = authentication_service.create_jwt_token(data={"sub": user.username}, expires_delta=access_token_expires)
    return {"access_token": access_token, "token_type": "bearer"}
@router.post("/refresh-token", response_model=Token)
async def refresh_access_token(current_user: str  = Depends(authentication_service.get_current_username)):
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = authentication_service.create_jwt_token(
        data={"sub": current_user}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

# Protected route
@router.get("/protected")
async def protected_route(current_user: str = Depends(authentication_service.get_current_user)):
    return {"message": f"Welcome, {current_user}!"}

@router.get("/me")
async def get_self_info(user: UserProfile = Depends(authentication_service.get_current_user)):
    if not user:
        raise HTTPException(status_code=500, detail="Unexpected error")
    user_info= UserInfo(username=user.username,
                        email=user.email,
                        first_name=user.first_name,
                        last_name=user.last_name,
                        phone_number=user.phone_number,
                        is_email_verified=user.is_email_verified,
                        private_account=user.private_account)

    return user_info
  

# gte another users info
@router.get("/{username}")
async def get_user_info(username: str, current_user: UserProfile = Depends(authentication_service.get_current_user)):
    # Check if the current user is allowed to access the requested user's information
    user = authentication_service.get_user(username)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    #if it is another user, the account
    if current_user.username != username and user.private_account:
        raise HTTPException(status_code=403, detail="User's information is private")

    # Users can access their own information, return it here 
    user_info= UserInfo(username=user.username,
                        email=user.email,
                        first_name=user.first_name,
                        last_name=user.last_name,
                        phone_number=user.phone_number,
                        is_email_verified=user.is_email_verified,
                        private_account=user.private_account)

    return user_info
