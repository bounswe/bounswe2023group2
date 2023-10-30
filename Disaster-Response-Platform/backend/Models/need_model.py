from pydantic import BaseModel, Field
from typing import Dict, Any
from enum import Enum


class Need(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    initialQuantity: int = Field(default=None)
    urgency: int = Field(default=None)
    unsuppliedQuantity: int = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    
class QuantityUpdate(BaseModel):
    quantity: int
