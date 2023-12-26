from pydantic import BaseModel, Field
from typing import Dict, Any, Optional, List
from enum import Enum
from Models.resource_model import *
from Models.need_model import *
import datetime
from bson.objectid import ObjectId
# Function to get current time in GMT+3
def current_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3)
    
class statusEnum(str, Enum): 
    continuing="Continuing"
    created= "Created"
    finished= "Finished"
    cancelled= "Cancelled"

class RecurringItem(str,Enum):
    need= 'Need'
    resource="Resource" 
    event = "Event"
    action = "Action"

class Recurrence(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    title: str = Field(default=None)
    description: str = Field(default=None)
    activity: RecurringItem= None
    status: statusEnum = Field(statusEnum.created)
    occurance_rate: int = Field(default=1) #1,2,3,4,5,6,7,8,9,10,11,12
    occurance_unit: str = Field(default="day") #day, week, month, year
    occurring_dates: List[int] = Field(default=[]) #1,2,3,4,5,6,7
    duration: int = Field(default=1) #1,2,3,4,5,6,7,8,9,10,11,12 
    recurring_items: List[str] = Field(default=[]) #ids of recurring items
    created_at: datetime.datetime = Field(default_factory=current_time_gmt3)
    last_updated_at: datetime.datetime = Field(default_factory=current_time_gmt3)
    start_at: datetime.datetime = Field(default=None)
    end_at: datetime.datetime = Field(default=None)
 
    @validator('end_at', 'start_at', pre=True)
    def convert_str_to_datetime(cls, value):
        if isinstance(value, str):
            try:
                return datetime.datetime.strptime(value, '%Y-%m-%d')
            except ValueError:
                raise ValueError("Incorrect date format, should be YYYY-MM-DD")
        return value

