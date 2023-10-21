from pydantic import BaseModel
from typing import Dict, Any
from enum import Enum

class ConditionEnum(str, Enum):
    new = "new"
    used = "used"

class Resource(BaseModel):
    _id: str
    created_by: str
    condition: ConditionEnum
    initialQuantity: int
    unsuppliedQuantity: int
    type: str
    specificAttributes: Dict[str, Any]
