from pydantic import BaseModel, Field
from typing import Dict, Any, Optional, List
from enum import Enum
from Models.resource_model import *
from Models.need_model import *
import datetime
from bson.objectid import ObjectId
# Function to get current time in GMT+3
def current_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3)
    
class statusEnum(str, Enum):
    created="created"
    active= "active"
    inprogress = "inprogress"
    inactive= "cancelled"
    done= "done"

class ActionType(str,Enum):
    transport="Transport" #resource taşımak
    distribute="Distribute" #resource need dağıtmak
    rescue="Rescue" # kurtarma
    other="Other" #resource ya da need ile ilgili başka bir eylem
    search_for_survivors="Search for survivors"
    dispatch_of_a_relief_team="dispatch of a relief team"
    need_resource="need_resource"


class ActivityType(str,Enum):
    cloth="cloth"
    food="food" 
    drink="drink"
    shelter="shelter"
    medication="medication"
    transportation="transportation"
    tool="tool"
    human="human"
    other="other"

class Action(BaseModel):
    _id: str = Field(default=None)
    title: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    type: ActionType= None
    status: statusEnum = Field(statusEnum.created)
    start_at: datetime.datetime = Field(default_factory=current_time_gmt3)
    created_at: datetime.datetime = Field(default_factory=current_time_gmt3)
    updated_at: datetime.datetime = Field(default_factory=current_time_gmt3)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    end_at: datetime.datetime = Field(default=None)
    location: str = Field(default=None)
    location_coordinates: List[float] = Field(default=None)
    resources: List[str] = Field(default_factory=list) #TODO group same activiy types
    needs: List[str] = Field(default_factory=list)
    recurrence: str = Field(default=None) # Object_id

    @validator('end_at', 'start_at', pre=True)
    def convert_str_to_datetime(cls, value):
        if isinstance(value, str):
            try:
                return datetime.datetime.strptime(value, '%Y-%m-%d')
            except ValueError:
                raise ValueError("Incorrect date format, should be YYYY-MM-DD")
        return value


class ActionSuccess(BaseModel):
    action_id: str


class updateResponse(BaseModel):
    actions: List[Action]

class doActionResponse(BaseModel):
    met_needs: List[int]

