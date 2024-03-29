from pydantic import BaseModel, Field, validator
from typing import Dict, Any, List
from enum import Enum
import datetime

from bson.objectid import ObjectId

class UnitEnum(str, Enum):
    kg = "kg"
    piece = "piece"
    portion = "portion"
    daily = "daily"
    lt = "lt"
    gram = "gram"
    box = "box"
    pack = "pack"

# Function to get current time in GMT+3
def current_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3)

class statusEnum(str, Enum):
    created="created"
    active= "active"
    inprogress = "inprogress"
    done= "done"

class Need(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    initialQuantity: int = Field(default=None)
    quantityUnit: UnitEnum = Field(default=UnitEnum.piece)
    urgency: int = Field(default=None)
    unsuppliedQuantity: int = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    open_address : str = Field(default=None)
    x: float = Field(default=None)
    y: float = Field(default=None)
    active: bool = Field(default=True)
    occur_at: datetime.datetime = Field(default=None)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    created_at: datetime.datetime = Field(default_factory=current_time_gmt3)
    last_updated_at: datetime.datetime = Field(default_factory=current_time_gmt3)

    verified_voter_username: str = Field(default = None)
    verified_vote_type: str = Field(default = None)
    action_list: List[str] = Field(default=[])
    status: statusEnum = Field(default='created')
    recurrence: str = Field(default = None)

    @validator('occur_at', pre=True)
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

class NeedList(BaseModel):
    needs: List[Dict]