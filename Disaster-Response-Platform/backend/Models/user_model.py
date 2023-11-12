from pydantic import BaseModel, EmailStr, constr, Field
from enum import Enum
from typing import Dict, Any

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
class Error(BaseModel):
    ErrorMessage: str
    ErrorDetail: str

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
class ProficiencyEnum(str, Enum):
    bilingual = "bilingual"
    doctor = "doctor"
    pharmacist ="pharmacist"
    rescue_member="rescue_member"
    infrastructure_engineer="infrastructure_engineer"
    it_specialist= "it_specialist"
    #police, soldier, not for human resource but searching certain info, 





class ProfRequest(BaseModel):
    username: str | None= None
    proficiency: ProficiencyEnum
    details: str

class UserProfile(User):
    first_name: str
    last_name: str
    phone_number: str | None= None
    is_email_verified: bool = False
    private_account: bool = False


class UserInfo(BaseModel):
    username: str 
    email: EmailStr | None = None
    first_name: str
    last_name: str
    phone_number: str | None= None
    is_email_verified: bool = False
    private_account: bool = False


class UpdateUserRequest(BaseModel):
    email: EmailStr | None = None
    first_name: str | None =None
    last_name: str  | None= None
    private_account: bool = False
    phone_number: constr(
        min_length=11,
        max_length=11,
        regex=r"^\d{11}$"
       
    ) = None

class SignUpSuccess(BaseModel):
    user: dict
    inserted_id: str

class ProfReqSuccess(BaseModel):
    proficiency: dict
    inserted_id: str