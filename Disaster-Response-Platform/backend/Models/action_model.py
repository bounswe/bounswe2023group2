from pydantic import BaseModel, Field
from typing import Dict, Any, Optional, List
from enum import Enum
from resource_model import *
from need_model import *

class statusEnum(str, Enum):
    active= "Active"
    inactive= "Inactive"

class ActionType(str,Enum):
    moving="Moving" #resource ya da need taşımak
    search_for_survivors="Search for survivors"
    dispatch_of_a_relief_team="dispatch of a relief team"
    need_resource="need_resource"

class Action(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    creation_time: float 
    details: Dict[str, Any] = Field(default=None)
    related_needs: Optional[List[Need]] = None
    related_resources: Optional[List[Resource]] = None
    comment: str = None
    recur: bool= None # recur ediyosa resource
    start_location_x: float = Field(default=0.0)
    start_location_y: float = Field(default=0.0)
    endLocation_x: float = Field(default=0.0)
    endLocation_y: float = Field(default=0.0)
    status: statusEnum = None
    type: ActionType= None
    

class ActionSuccess(BaseModel):
    action_id: int
