from pydantic import BaseModel
from typing import Dict, Any
from enum import Enum
from bson.objectid import ObjectId

class ConditionEnum(str, Enum):
    new = "new"
    used = "used"

class Resource(BaseModel):
    _id: ObjectId
    created_by: str
    condition: ConditionEnum
    initialQuantity: int
    unsuppliedQuantity: int
    type: str
    details: Dict[str, Any]
