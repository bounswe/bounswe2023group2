from fastapi import APIRouter, HTTPException, Response, Depends, status
from fastapi.security import OAuth2PasswordBearer,OAuth2PasswordRequestForm
from jose import JWTError, jwt
from typing import Optional
from datetime import datetime, timedelta
from Services import authentication
from fastapi.responses import JSONResponse,

import config
from Database.mongo import MongoDB
from Models import user_model


router = APIRouter()
db = MongoDB.getInstance()
# Secret key to sign and verify the JWT token
SECRET_KEY = config.SECRET_KEY
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

users_collection = MongoDB.get_collection('users')

@router.post("/signup", response_model= user_model.RegisteredUser)
async def signup(currentUser :user_model.RegisteredUser):
    try:
        userDb = db.get_collection("authenticated_user") 
        if (userDb.find_one({"username":currentUser.username}) !=None) : #if there is a user already existed with current username
            return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Username already taken")
        elif (len(currentUser.password) < 8) : #if username is not existed in db but password contains less than 8 characters
            return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Password cannot be less than 8 characters")
        elif (len(currentUser.phone_number)!= 11 or currentUser.phone_number[:2]!= "05"): #if phone number is not valid
            return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Phone number format is wrong")
        #if password has at least 8 characters and username isn't used by another user, insert it to db
        userDb.insert_one({"username":currentUser.username, "first_name": currentUser.first_name, "last_name": currentUser.last_name, "email":currentUser.email, "phone_number":currentUser.phone_number, "password":currentUser.password})
        print("+9"+currentUser.phone_number)
        return currentUser
    except Exception as e:
        # Handle any other unexpected exceptions here
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail="An unexpected error occurred")

# Login route
@router.post("/token", response_model=user_model.Token)
async def login_for_access_token(user: user_model.User): ##douıble check here
    user = authentication.authenticate_user(users_collection, user.username, user.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = authentication.create_jwt_token(data={"sub": user.username}, expires_delta=access_token_expires)
    return {"access_token": access_token, "token_type": "bearer"}

@router.post("/refresh-token", response_model=user_model.Token)
async def refresh_access_token(token_data: user_model.Token = Depends(authentication.get_current_user)):
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = authentication.create_jwt_token(
        data={"sub": token_data.username}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}
# Protected route
@router.get("/protected")
async def protected_route(current_user: str = Depends(authentication.get_current_user)):
    return {"message": f"Welcome, {current_user}!"}

@router.get("/users/me/", response_model=user_model.User)
async def read_users_me(current_user: user_model.User = Depends(authentication.get_current_active_user)):
    return current_user


@router.get("/users/me/items/")
async def read_own_items(current_user: user_model.User = Depends(authentication.get_current_active_user)):
    return [{"item_id": "Foo", "owner": current_user.username}]

@router.exception_handler(HTTPException)
async def http_exception_handler(request, exc):
    return JSONResponse(status_code=exc.status_code, content={"error": exc.detail})