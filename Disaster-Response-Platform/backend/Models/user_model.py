from pydantic import BaseModel, EmailStr
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

class UserInDB(User):
    hashed_password: str

class RegisterUser(User):
    first_name: str
    last_name: str
    phone_number: str
    is_email_verified: bool = False
    private_account: bool = False
    password: str

class RegisteredUser(UserInDB):
    first_name: str
    last_name: str
    phone_number: str
    is_email_verified: bool = False
    private_account: bool = False


