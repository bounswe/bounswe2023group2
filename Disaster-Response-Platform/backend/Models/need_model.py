from pydantic import BaseModel, Field, validator
from typing import Dict, Any
from enum import Enum
import datetime

class Need(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    initialQuantity: int = Field(default=None)
    urgency: int = Field(default=None)
    unsuppliedQuantity: int = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    recurrence_id: str = Field(default = None)
    recurrence_rate: str = Field(default=None)
    recurrence_deadline: datetime.datetime = Field(default=None)
    x: float = Field(default=0.0)
    y: float = Field(default=0.0)
    active: bool = Field(default=True)
    occur_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    created_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    last_updated_at: datetime.datetime = Field(default_factory=datetime.datetime.now)

    @validator('recurrence_deadline', 'occur_at', pre=True)
    def convert_str_to_datetime(cls, value):
        if isinstance(value, str):
            try:
                return datetime.datetime.strptime(value, '%Y-%m-%d')
            except ValueError:
                raise ValueError("Incorrect date format, should be YYYY-MM-DD")
        return value
    
class QuantityUpdate(BaseModel):
    quantity: int
    
class UrgencyUpdate(BaseModel):
    urgency: int

# class voteUpdate(BaseModel):
#     vote: int

