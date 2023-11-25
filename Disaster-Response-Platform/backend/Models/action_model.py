from pydantic import BaseModel, Field
from typing import Dict, Any, Optional, List
from enum import Enum
from Models.resource_model import *
from Models.need_model import *
import datetime

class statusEnum(str, Enum):
    created="Created"
    active= "Active"
    inactive= "Cancelled"

class ActionType(str,Enum):
    moving="Moving" #resource ya da need taşımak
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


class ActionGroup(BaseModel):
    recurrence: bool= None
    group_type: str= None
    related_needs: Optional[List[str]]
    related_resources: Optional[List[str]]
    
class Action(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    type: ActionType= None
    start_location_x: float = Field(default=0.0)
    start_location_y: float = Field(default=0.0)
    endLocation_x: float = Field(default=0.0)
    endLocation_y: float = Field(default=0.0)
    status: statusEnum = Field(statusEnum.created)
    occur_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    created_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    last_updated_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    related_groups: Optional[List[ActionGroup]]= None
 
    end_at: datetime.datetime = Field(default_factory=datetime.datetime.now) #recurrence ise, girilen date min(resource, need) den büyük ise user a action bilgisi tarih içererek dönülür
   
    @validator('end_at', 'occur_at', pre=True)
    def convert_str_to_datetime(cls, value):
        if isinstance(value, str):
            try:
                return datetime.datetime.strptime(value, '%Y-%m-%d')
            except ValueError:
                raise ValueError("Incorrect date format, should be YYYY-MM-DD")
        return value


class groupCheckResponse(BaseModel):
    type: str
    resourceTexts: List[str]  #her bir resource için ayrı text "12.10.23 ten 22.10.23 e kadar her gün"
    needTexts: List[str] #her bir need için ayrı text "12.10.23 te"

class ActionSuccess(BaseModel):
    action_id: str

class ActivityInfo(BaseModel):
    text: str

class AllActionsResponse(BaseModel):
    notsure: str

class updateResponse(BaseModel):
    actions: List[Action]

class doActionResponse(BaseModel):
    met_needs: List[int]