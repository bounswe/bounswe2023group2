from fastapi import APIRouter, HTTPException, Response, Depends, status
from fastapi.security import OAuth2PasswordBearer,OAuth2PasswordRequestForm
from jose import JWTError, jwt
from typing import Optional
from datetime import datetime, timedelta
from Services import authentication_service
from fastapi.responses import JSONResponse

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
userDb = MongoDB.get_collection('authenticated_user')


@router.post("/signup")
async def signup(currentUser :user_model.RegisterUser):
    try:
         
        if (userDb.find_one({"username":currentUser.username}) !=None) : #if there is a user already existed with current username
            return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Username already taken")
        if (userDb.find_one({"email":currentUser.email}) !=None):
            return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Email already taken")
        if (not authentication_service.is_valid_password(currentUser.password)) : #if username is not existed in db but password contains less than 8 characters
            return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Password cannot be less than 8 characters")
        if (not authentication_service.is_valid_phone_number(currentUser.phone_number)): #if phone number is not valid
            return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Phone number format is wrong")
        print("hash created")
        hash= authentication_service.get_password_hash(currentUser.password)
        print("hash created")
        insert_result = userDb.insert_one({
            "username": currentUser.username,
            "first_name": currentUser.first_name,
            "last_name": currentUser.last_name,
            "email": currentUser.email,
            "phone_number": currentUser.phone_number,
            "hashed_password": hash
        })

        #todo what should itreturn, my brain does not process anymore
        return {"status": "OK"}

    except Exception as e:
        # Handle any other unexpected exceptions here
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail="An unexpected error occurred")

# Login route
@router.post("/login", response_model=user_model.Token)
async def login_for_access_token(user: user_model.User): ##douıble check here
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

@router.post("/refresh-token", response_model=user_model.Token)
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

@router.get("/users/me/", response_model=user_model.User)
async def read_users_me(current_user: user_model.User = Depends(authentication_service.get_current_active_user)):
    return current_user


@router.get("/users/me/items/")
async def read_own_items(current_user: user_model.User = Depends(authentication_service.get_current_active_user)):
    return [{"item_id": "Foo", "owner": current_user.username}]

