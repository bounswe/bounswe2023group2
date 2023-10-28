from fastapi import APIRouter, HTTPException, Response, Depends, status
from fastapi.security import OAuth2PasswordBearer,OAuth2PasswordRequestForm
from jose import JWTError, jwt
from typing import Optional
from datetime import datetime, timedelta
from Services import authentication_service
from fastapi.responses import JSONResponse

import config
from Database.mongo import MongoDB
from Models.user_model import *


router = APIRouter()
db = MongoDB.getInstance()
# Secret key to sign and verify the JWT token
SECRET_KEY = config.SECRET_KEY
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 120
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")
userDb = MongoDB.get_collection('authenticated_user')


@router.post("/signup", status_code=status.HTTP_201_CREATED)
async def signup(currentUser : CreateUserRequest):
    try:
        return authentication_service.create_user(currentUser)
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail="An unexpected error occurred while creating user")

# Login route
@router.post("/login", response_model=Token, status_code=200)
async def login_for_access_token(user: User): ##douıble check here
    user = authentication_service.authenticate_user(user.username, user.password)
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
async def refresh_access_token(current_user: str  = Depends(authentication_service.get_current_user)):
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = authentication_service.create_jwt_token(
        data={"sub": current_user}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}
# Protected route
@router.get("/protected")
async def protected_route(current_user: str = Depends(authentication_service.get_current_user)):
    return {"message": f"Welcome, {current_user}!"}

#@router.get("/users/me/", response_model=user_model.User)
async def read_users_me(current_user: User = Depends(authentication_service.get_current_active_user)):
    return current_user


#@router.get("/users/me/items/")
async def read_own_items(current_user: User = Depends(authentication_service.get_current_active_user)):
    return [{"item_id": "Foo", "owner": current_user.username}]

