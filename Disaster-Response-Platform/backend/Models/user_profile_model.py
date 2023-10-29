import bson
from pydantic import BaseModel, Field
from typing import Dict, Any
from enum import Enum
from datetime import date

class EducationEnum(str, Enum):
    ilk = "ilk"
    orta = "orta"
    lise = "lise"
    yuksekokul = "yuksekokul"
    universite = "universite"

class BloodTypeEnum(str, Enum):
    a_pos = "A Rh+"
    b_pos = "B Rh+"
    z_pos = "0 Rh+"
    AB_pos = "AB Rh+"
    a_neg = "A Rh-"
    b_neg = "B Rh-"
    z_neg = "0 Rh-"
    AB_neg = "AB Rh-"

class SkillLevel(str, Enum):
    beginner = "beginner"
    basic = "basic"
    intermediate = "intermediate"
    high = "skilled"
    expert = "expert"

class LanguageLevel(str, Enum):
    beginner = "beginner"
    intermediate = "intermediate"
    advanced = "advanced"
    native = "native"

class UserOptionalInfo(BaseModel):
    username: str = Field(default=None)
    date_of_birth: date = Field(default=None)
    nationality: str= Field(default=None)
    #profile_picture: bson = Field(default=None)
    identity_number: str= Field(default=None)
    education: EducationEnum = Field(default=None)
    health_condition: str = Field(default=None)
    blood_type: BloodTypeEnum = Field(default=None)
    Address: str= Field(default=None)

class UserSocialMediaLink(BaseModel):
    username: str = Field(default=None)
    platform_name: str
    profile_URL: str

class UserSkill(BaseModel):
    username: str = Field(default=None)
    skill_definition: str
    skill_level: SkillLevel = Field(default=None)
    #skill_document: bson = Field(default=None)

class UserLanguage(BaseModel):
    username: str = Field(default=None)
    language: str
    language_level: LanguageLevel = Field(default=None)
