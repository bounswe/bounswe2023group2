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
    x: float = Field(default=0.0)
    y: float = Field(default=0.0)
    
class QuantityUpdate(BaseModel):
    quantity: int
    
class UrgencyUpdate(BaseModel):
    urgency: int
