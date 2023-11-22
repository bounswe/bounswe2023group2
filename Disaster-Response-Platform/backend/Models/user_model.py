from pydantic import BaseModel, EmailStr, constr, Field
from enum import Enum, auto
from typing import Dict, Any
from typing import Optional, List
class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    username: str | None = None

class UserRole(Enum):
    GUEST = "GUEST"
    AUTHENTICATED = "AUTHENTICATED"
    ROLE_BASED = "ROLE_BASED"
    CREDIBLE = "CREDIBLE"
    ADMIN = "ADMIN"

class User(BaseModel):
    username: str 
    email: EmailStr | None = None
    disabled: bool | None = None
    password:str
    #user_role: UserRole = Field(default=None)


class LoginUserRequest(BaseModel):
    username_or_email_or_phone: str
    password:str
class Error(BaseModel):
    ErrorMessage: str
    ErrorDetail: str
class ProficiencyEnum(str, Enum):
    bilingual = "bilingual"
    doctor = "doctor"
    pharmacist ="pharmacist"
    rescue_member="rescue_member"
    infrastructure_engineer="infrastructure_engineer"
    it_specialist= "it_specialist"
    other="other"
    #police, soldier, not for human resource but searching certain info, 

class ProfRequest(BaseModel):
    proficiency: Optional[ProficiencyEnum] = None
    details: str=None

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
    user_role: UserRole = Field(default=None)
    proficiency: Optional[List[ProfRequest]] = None





class UserProfile(User):
    first_name: str
    last_name: str
    phone_number: str | None= None
    is_email_verified: bool = False
    private_account: bool = False
    user_role: UserRole = Field(default=None)
    proficiency: Optional[List[ProfRequest]] = None



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
    proficiency: ProficiencyEnum= Field(default=None)


class UserRoleResponse(BaseModel):
    user_role: str