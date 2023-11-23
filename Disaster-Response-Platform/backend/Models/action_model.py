from pydantic import BaseModel, Field
from typing import Dict, Any, Optional, List
from enum import Enum
from resource_model import *
from need_model import *

class statusEnum(str, Enum):
    created="Created"
    active= "Active"
    inactive= "Cancelled"

class ActionType(str,Enum):
    moving="Moving" #resource ya da need taşımak
    search_for_survivors="Search for survivors"
    dispatch_of_a_relief_team="dispatch of a relief team"
    need_resource="need_resource"

class Action(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    type: ActionType= None
    start_location_x: float = Field(default=0.0)
    start_location_y: float = Field(default=0.0)
    endLocation_x: float = Field(default=0.0)
    endLocation_y: float = Field(default=0.0)
    status: statusEnum = None
    occur_at: datetime.date = Field(default_factory=datetime.date.today)
    created_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    last_updated_at: datetime.datetime = Field(default_factory=datetime.datetime.now)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    related_needs: Optional[List[Need]] #need_resource type ı için en az bir tane olmalı
    related_resources: Optional[List[Resource]] #en az bir tane olmalı
    end_at: datetime.date = Field(default=None) #recurrence ise, girilen date min(resource, need) den büyük ise user a action bilgisi tarih içererek dönülür


    
    


class ActionSuccess(BaseModel):
    action_id: int

class ActivityInfo(BaseModel):
    text: str

class AllActionsResponse(BaseModel):
    notsure: str

class updateResponse(BaseModel):
    actions: List[Action]