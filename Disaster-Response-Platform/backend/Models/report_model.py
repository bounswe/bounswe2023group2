from pydantic import BaseModel, Field
from typing import Dict, Any
from enum import Enum


class Report(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    

