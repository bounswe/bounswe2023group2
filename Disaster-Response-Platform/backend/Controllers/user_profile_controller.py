from fastapi import APIRouter, HTTPException, Response, Depends, status
from fastapi.security import OAuth2PasswordBearer,OAuth2PasswordRequestForm
from jose import JWTError, jwt
from typing import Optional
from datetime import datetime, timedelta
from Services import authentication
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




# Login route
@router.post("/token", response_model=user_model.Token)
async def login_for_access_token(user: user_model.User): ##douıble check here
    user = authentication.authenticate_user(users_collection, form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = authentication.create_jwt_token(data={"sub": user.username}, expires_delta=access_token_expires)
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