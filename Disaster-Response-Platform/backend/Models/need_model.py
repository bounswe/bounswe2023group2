from pydantic import BaseModel, Field
from typing import Dict, Any
from enum import Enum
import datetime

class Need(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    initialQuantity: int = Field(default=None)
    urgency: int = Field(default=None)
    unsuppliedQuantity: int = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    x: float = Field(default=0.0)
    y: float = Field(default=0.0)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    created_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    last_updated_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    
class QuantityUpdate(BaseModel):
    quantity: int
    
class UrgencyUpdate(BaseModel):
    urgency: int

# class voteUpdate(BaseModel):
#     vote: int

