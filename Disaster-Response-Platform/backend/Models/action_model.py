from pydantic import BaseModel, Field
from typing import Dict, Any, Optional, List
from enum import Enum
from resource_model import *
from need_model import *


class Action(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    x: float = Field(default=0.0)
    y: float = Field(default=0.0)
    needs: Optional[List[Need]] = None
    resources: Optional[List[Resource]] = None
    comment: str = None


class ActionSuccess(BaseModel):
    action_id: int