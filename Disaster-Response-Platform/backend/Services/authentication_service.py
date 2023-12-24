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
from fastapi.security import HTTPBearer


pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


SECRET_KEY = config.SECRET_KEY
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")
auth_scheme = HTTPBearer()
userDb = MongoDB.get_collection('authenticated_user')
# Verify JWT token
def get_current_user(token: str = Depends(auth_scheme)):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
  
    try:
        # if not  token.credentials is None:
        #     token = token.credentials
        # payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        if not isinstance(token, str) and not  token.credentials is None:
            token = token.credentials
         

            # If token is an instance of Token, access its credentials attribute
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None:
         
            raise credentials_exception
    except JWTError:
        raise credentials_exception
    user = get_user(username)
    if user is None:
        raise credentials_exception

    return user

def get_current_admin_user(current_user: UserProfile = Depends(get_current_user)):
    if current_user.user_role.value != "ADMIN":
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail=f'Only admins have access to this route')
    return current_user.user_role

def get_current_username(current_user: LoginUserRequest= Depends(get_current_user)):
    return current_user.username

def get_current_email(current_user: LoginUserRequest= Depends(get_current_user)):
    if current_user.email:
        return current_user.email
    else:
        raise HTTPException(status_code=404, detail="Email not found for the current user")

def update_user(username: str, updated_user: UpdateUserRequest):
 
    
    if updated_user.email != userDb[username]["email"] :
        query = {"username": username}
        update_operation = {"$set": {"is_email_verified": False}}
        result = userDb.update_one(query, update_operation)
    if updated_user.email is not None:
        query = {"username": username}
        update_operation = {"$set": {"email": updated_user.email}}
        result = userDb.update_one(query, update_operation)
    if updated_user.first_name is not None:
        query = {"username": username}
        update_operation = {"$set": {"first_name": updated_user.first_name}}
        result = userDb.update_one(query, update_operation)
    if updated_user.last_name is not None:
        query = {"username": username}
        update_operation = {"$set": {"last_name": updated_user.last_name}}
        result = userDb.update_one(query, update_operation)
    if updated_user.private_account is not None:
        query = {"username": username}
        update_operation = {"$set": {"private_account": updated_user.private_account}}
        result = userDb.update_one(query, update_operation)

    if updated_user.phone_number is not None:
        if (not is_valid_phone_number(updated_user.phone_number)): #if phone number is not valid
            raise ValueError("Phone number must be 11 digits and start with '05'")
        
        query = {"username": username}
        update_operation = {"$set": {"phone_number": updated_user.phone_number}}
        result = userDb.update_one(query, update_operation)
    user = get_user(username)
    user_info= UserInfo(username=user.username,
                        email=user.email,
                        first_name=user.first_name,
                        last_name=user.last_name,
                        phone_number=user.phone_number,
                        is_email_verified=user.is_email_verified,
                        private_account=user.private_account)

    return user_info



# Create a JWT token
def create_jwt_token(data: dict, expires_delta: timedelta):
    to_encode = data.copy()
    expire = datetime.utcnow() + expires_delta
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    user= get_current_user(encoded_jwt)
    if user.proficiency is not None:
        return LoginResponse(access_token= encoded_jwt, token_type='Bearer', user_role=user.user_role, proficiency= user.proficiency )
    return LoginResponse(access_token= encoded_jwt, token_type='Bearer', user_role=user.user_role )

def create_user(user: CreateUserRequest):

    if not (all([user.username,user.password, user.first_name, user.last_name] )) or not (user.phone_number or user.email) :
        raise ValueError("Please fill all mandatory fields")     
       
    if (userDb.find_one({"username": user.username}) !=None) : #if there is a user already existed with current username
        raise ValueError("Username already taken")
    
    if (user.email and userDb.find_one({"email": user.email}) !=None):
        raise ValueError("Email already taken")
    if (user.phone_number and userDb.find_one({"phone_number": user.phone_number}) !=None):
        raise ValueError("phone_number already taken")
       
    if (not is_valid_password(user.password)) : #if username is not existed in db but password contains less than 8 characters
        raise ValueError("Password should include a digit")
    
    if (user.phone_number and not is_valid_phone_number(user.phone_number)): #if phone number is not valid
        raise ValueError("Phone number must be 11 digits and start with '05'")
        
    hash= get_password_hash(user.password)
    user.password=hash
    user.proficiency= {}
    user.user_role= UserRole.GUEST.value #default signed up user is authenticated
  
    insert_result = userDb.insert_one(user.dict())

    if insert_result.inserted_id:
        success_response = SignUpSuccess(
            user=dict(user),
            inserted_id=str(insert_result.inserted_id)  # Assuming 'result' is the inserted_id
        )
        return success_response
    else:
        raise ValueError("User could not be created")


def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password):
    return pwd_context.hash(password)

def get_user(username_or_email_or_phone: str):
    #user_document = userDb.find_one({"username": username})
    user_document = userDb.find_one({
        "$or": [
            {"username": username_or_email_or_phone},
            {"email": username_or_email_or_phone},
            {"phone_number": username_or_email_or_phone}
        ]
    })
    if user_document is not None:
        return UserProfile(**user_document)

def authenticate_user(username_or_email_or_phone: str, password: str):
    user = get_user(username_or_email_or_phone)
    if not user:
        return False
    
    if not verify_password(password, user.password):
        return False
    return user


async def get_current_active_user(
    current_user: LoginUserRequest= Depends(get_current_user)
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


def verify_user(username: str):
    user = get_user(username)
    if not user:
        raise ValueError(f"No such user with username {username}")
    
    update_result = userDb.update_one(
    {"username": username},
    {"$set": {"user_role": "CREDIBLE"}}
    )    
    return True

def unverify_user(username: str):
    user = get_user(username)
    if not user:
        raise ValueError(f"No such user with username {username}")
    
    update_result = userDb.update_one(
    {"username": username},
    {"$set": {"user_role": "AUTHENTICATED"}}
    )    
    return True
    
def unauthorize_user(username: str):
    user = get_user(username)
    if not user:
        raise ValueError(f"No such user with username {username}")
    
    update_result = userDb.update_one(
    {"username": username},
    {"$set": {"user_role": "GUEST"}}
    )    
    return True

def is_admin(username: str):
    user = get_user(username)
    return (user.user_role == user.user_role.ADMIN)