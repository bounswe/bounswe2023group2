from pydantic import BaseModel, Field
from typing import Dict, Any
from enum import Enum


class Feedback(BaseModel):
    _id: str = Field(default=None)
    entityType: str = Field(default=None)
    entityID: str = Field(default=None)
    username: str = Field(default=None)
    vote: str = Field(default=None)
    
    
    
class VoteUpdate(BaseModel):
    entityType: str
    entityID: str
    
# class UrgencyUpdate(BaseModel):
#     urgency: int
