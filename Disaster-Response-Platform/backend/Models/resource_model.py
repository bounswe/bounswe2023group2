from pydantic import BaseModel, Field
from typing import Dict, Any
from enum import Enum

class ConditionEnum(str, Enum):
    new = "new"
    used = "used"

class Resource(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    condition: ConditionEnum = Field(default=None)
    initialQuantity: int = Field(default=None)
    unsuppliedQuantity: int = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)