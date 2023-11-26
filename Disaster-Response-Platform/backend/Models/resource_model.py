from pydantic import BaseModel, Field, validator
from typing import Dict, Any, List
from enum import Enum
import datetime

class ConditionEnum(str, Enum):
    new = "new"
    used = "used"

class Recurrence(Enum):
    Daily= 1
    Weekly= 7

class ActionHistory(BaseModel):
    quantity: int = Field(default=0)
    action_id: str= Field(None)

class Resource(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    condition: ConditionEnum = Field(default=None)
    initialQuantity: int = Field(default=None)
    currentQuantity: int = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    recurrence_id: str = Field(default=None)
    recurrence_rate: Recurrence = Field(default=None)
    recurrence_deadline: datetime.datetime = Field(default=None)
    x: float = Field(default=0.0)
    y: float = Field(default=0.0)
    active: bool = Field(default=True)
    occur_at: datetime.datetime = Field(default=None)
    created_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    last_updated_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    actions_used: List[ActionHistory]= Field(default=None)

    @validator('recurrence_deadline', 'occur_at', pre=True)
    def convert_str_to_datetime(cls, value):
        if isinstance(value, str):
            try:
                return datetime.datetime.strptime(value, '%Y-%m-%d')
            except ValueError:
                raise ValueError("Incorrect date format, should be YYYY-MM-DD")
        return value
    
# Update Body Models
class QuantityUpdate(BaseModel):
    quantity: int

class ConditionUpdate(BaseModel):
    condition: ConditionEnum

