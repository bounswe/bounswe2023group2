from Database.mongo import MongoDB
from fastapi.security import OAuth2PasswordBearer
from datetime import *
from fastapi import HTTPException, Depends, status
from jose import JWTError, jwt
from passlib.context import CryptContext
from Models.user_model import *
from typing import Annotated
import config
import json



pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


SECRET_KEY = config.SECRET_KEY
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")

userDb = MongoDB.get_collection('authenticated_user')
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
    user = get_user(username=username)
    if user is None:
        raise credentials_exception
    return user.username

# Create a JWT token
def create_jwt_token(data: dict, expires_delta: timedelta):
    to_encode = data.copy()
    expire = datetime.utcnow() + expires_delta
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

def create_user(user: CreateUserRequest):
        # Manual validation for required fields during creation
    if not all([user.username, user.email,user.password, user.first_name, user.last_name, user.phone_number]):
        raise ValueError("All fields are mandatory for user creation.")
    if (userDb.find_one({"username": user.username}) !=None) : #if there is a user already existed with current username
        return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Username already taken")
    if (userDb.find_one({"email": user.email}) !=None):
        return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Email already taken")
    if (not is_valid_password(user.password)) : #if username is not existed in db but password contains less than 8 characters
        return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Password should include a digit")
    if (not is_valid_phone_number(user.phone_number)): #if phone number is not valid
        return HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Phone number must be 11 digits and start with '05'")

    hash= get_password_hash(user.password)
    user.password=hash
    insert_result = userDb.insert_one(user.dict())
    print(user)
    if insert_result.inserted_id:
        return "{\"Users\":[" + json.dumps(dict(user)) + "], \"inserted_id\": " + f"\"{insert_result.inserted_id}\"" + "}"
    else:
        raise ValueError("User could not be created")


def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password):
    return pwd_context.hash(password)

def get_user(username: str):
    user_document = userDb.find_one({"username": username})
    if user_document is not None:
        
        return User(**user_document)

def authenticate_user(username: str, password: str):
    user = get_user(username)
    if not user:
        return False
    if not verify_password(password, user.hashed_password):
        return False
    return user

async def get_current_active_user(
    current_user: User= Depends(get_current_user)
):
    if current_user.disabled:
        raise HTTPException(status_code=400, detail="Inactive user")
    return current_user

def is_valid_phone_number(phone_number):
   #can add more checks here
    return phone_number.startswith("05")

def is_valid_password(password):
   #can add more checks here
    if not any(char.isdigit() for char in password):
        return False
    return True
    # if not re.search(r"[!@#$%^&*(),.?\":{}|<>]", password):
    #     return False
        # if not any(char.isupper() for char in password):
    #     return False

    # if not any(char.islower() for char in password):
    #     return False
