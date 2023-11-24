import bson
import pydantic
from pydantic import BaseModel, Field
from typing import Dict, Any, List
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

class ProfessionLevel(str, Enum):
    amateur = "amateur"
    pro = "pro"
    certified_pro = "certified pro"


class UserOptionalInfo(BaseModel):
    username: str = Field(default=None)
    date_of_birth: date = Field(default=None)
    nationality: str= Field(default=None)
    user_picture: pydantic.AnyUrl = Field(default=None)
    identity_number: str= Field(default=None)
    education: EducationEnum = Field(default=None)
    health_condition: str = Field(default=None)
    blood_type: BloodTypeEnum = Field(default=None)
    Address: str= Field(default=None)

class UserSocialMediaLink(BaseModel):
    username: str = Field(default=None)
    platform_name: str = Field(default=None)
    profile_URL: str = Field(default=None)

class UserSkill(BaseModel):
    username: str = Field(default=None)
    skill_definition: str
    skill_level: SkillLevel = Field(default=None)
    skill_document: pydantic.AnyUrl = Field(default=None)

class UserLanguage(BaseModel):
    username: str = Field(default=None)
    language: str
    language_level: LanguageLevel = Field(default=None)

class UserProfession(BaseModel):
    username: str = Field(default=None)
    profession: str
    profession_level: ProfessionLevel = Field(default=None)

class Professions(BaseModel):
    professions: List[UserProfession]

class Languages(BaseModel):
    languages: List[UserLanguage]

class UserSkills(BaseModel):
    skills: List[UserSkill]

class UserSocialMediaLinks(BaseModel):
    user_socialmedia_links: List[UserSocialMediaLink]

class UserOptionalInfos(BaseModel):
    user_optional_infos: List[UserOptionalInfo]


