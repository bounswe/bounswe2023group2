from pydantic import BaseModel, EmailStr, constr
from enum import Enum


class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    username: str | None = None

class User(BaseModel):
    username: str 
    email: EmailStr | None = None
    disabled: bool | None = None
    password:str
    

class LoginUserRequest(BaseModel):
    username_or_email_or_phone: str
    password:str
   

class CreateUserRequest(BaseModel):
    username: str
    first_name: str
    last_name: str
    phone_number: constr(
        min_length=11,
        max_length=11,
        regex=r"^\d{11}$"
       
    ) = None
    is_email_verified: bool = False
    private_account: bool = False
    email: EmailStr | None = None
    password: constr(
        min_length=8,
    )

class UserProfile(User):
    first_name: str
    last_name: str
    phone_number: str | None= None
    is_email_verified: bool = False
    private_account: bool = False




