from pydantic import BaseModel, Field
from typing import Dict, Any, List
from enum import Enum

class ProfileInfoApi(BaseModel):
    base_url:str = "/api/profiles"
    prefix:str =  Field(default=None)
    #route_path:str =  Field(default=None)
    name:str = Field(default=None)

