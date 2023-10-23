
from Database.mongo import MongoDB
from fastapi.security import OAuth2PasswordBearer
from datetime import *
import pymongo
from fastapi import HTTPException, Depends, status
from jose import JWTError, jwt
from passlib.context import CryptContext
from Models import user_model
from typing import Annotated

db = MongoDB.getInstance()
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
users_collection = MongoDB.get_collection('users')

SECRET_KEY = "your-secret-key" #todo: put it to config
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")


# Verify JWT token
def get_current_user(token: str = Depends(oauth2_scheme)):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
    except JWTError:
        raise credentials_exception
    user = get_user(users_collection, username=username)
    if user is None:
        raise credentials_exception
    return user

# Create a JWT token
def create_jwt_token(data: dict, expires_delta: timedelta):
    to_encode = data.copy()
    expire = datetime.utcnow() + expires_delta
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password):
    return pwd_context.hash(password)

def get_user(db, username: str):
    if username in users_collection:
        user_dict = users_collection[username]
        return user_model.UserInDB(**user_dict)

def authenticate_user(users_collection, username: str, password: str):
    user = get_user(users_collection, username)
    if not user:
        return False
    if not verify_password(password, user.hashed_password):
        return False
    return user

async def get_current_active_user(
    current_user: user_model.User= Depends(get_current_user)
):
    if current_user.disabled:
        raise HTTPException(status_code=400, detail="Inactive user")
    return current_user

def is_valid_phone_number(phone_number):
   #can add more checks here
    return len(phone_number) == 11 and phone_number.startswith("05")

def is_valid_password(password):
   #can add more checks here
    if len(password) < 8:
        return False
    # if not any(char.isupper() for char in password):
    #     return False

    # if not any(char.islower() for char in password):
    #     return False
    if not any(char.isdigit() for char in password):
        return False
    return True
    # if not re.search(r"[!@#$%^&*(),.?\":{}|<>]", password):
    #     return False
