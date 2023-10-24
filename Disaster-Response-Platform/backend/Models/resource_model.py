from pydantic import BaseModel, Field
from typing import Dict, Any
from enum import Enum

class ConditionEnum(str, Enum):
    new = "new"
    used = "used"

# Predefined Subtypes
class cloth(BaseModel):
    size: str     
    gender: str   
    age: str      
    subtype: str 

class food(BaseModel):
    expiration_date: str 
    allergens: str       
    subtype: str         

class shelter(BaseModel):
    number_of_people: int 
    weather_condition: str
    
class medication(BaseModel):
    disease_name: str   
    medicine_name: str  
    age: int           

class transportation(BaseModel):
    start_location: str
    end_location: str
    
class tool(BaseModel):
    tool_type: str   
    estimated_weight: int
    
class human(BaseModel):
    proficiency: str 
    number_of_people: int
    subtype: str    

class Resource(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    condition: ConditionEnum = Field(default=None)
    initialQuantity: int = Field(default=None)
    currentQuantity: int = Field(default=None)
    type: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)