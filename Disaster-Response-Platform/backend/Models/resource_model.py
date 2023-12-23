from pydantic import BaseModel, Field, validator
from typing import Dict, Any, List
from enum import Enum
import datetime

# Function to get current time in GMT+3
def current_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3)

class ConditionEnum(str, Enum):
    new = "new"
    used = "used"

class ActionHistory(BaseModel):
    quantity: int = Field(default=0)
    action_id: str= Field(None)


class Resource(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    initialQuantity: int = Field(default=None)
    currentQuantity: int = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    x: float = Field(default=None)
    y: float = Field(default=None)
    active: bool = Field(default=True)
    occur_at: datetime.datetime = Field(default=None)
    created_at: datetime.datetime = Field(default_factory=current_time_gmt3)
    last_updated_at: datetime.datetime = Field(default_factory=current_time_gmt3)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    actions_used: List[ActionHistory]= Field(default=None)
    action_list: List[str]= Field(default=[])
    recurrence: str= Field(default = None)

    @validator('occur_at', pre=True)
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

class ResourceList(BaseModel):
    resources: List[Dict]