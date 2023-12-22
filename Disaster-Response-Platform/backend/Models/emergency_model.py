from enum import Enum

from pydantic import BaseModel, Field
from typing import List
from datetime import date, datetime

# 1.2.3.6.1.1 Type: Can be News, Debris, Infrastructure, Disaster, Help-Arrived (Check that other than News, types are of Event Types)
# 1.2.3.6.1.2 Location
# 1.2.3.6.1.3 Contact Number
# 1.2.3.6.1.4 Notes
# 1.2.3.6.1.5 Is Active
# 1.2.3.6.1.6 Is verified
# 1.2.3.6.1.7 Number of Upvotes
# 1.2.3.6.1.8 Number of Downvotes
# 1.2.3.6.1.9 Creation time
# 1.2.3.6.1.10 Creator username
# 1.2.3.6.1.11 Dealt by username
# 1.2.3.6.1.12 Dealt at time
# 1.2.3.6.1.13 Is debunked
# 1.2.3.6.1.14 Verification notes
# 1.2.3.6.1.15 Updated time

class EmergencyTypesEnum(str, Enum):
    news = "News"
    debris = "Debris"
    infrastructure = "Infrastructure"
    disaster = "Disaster"
    helparrived = "Help-Arrived"


class Emergency(BaseModel):
    _id: str = Field(default=None)
    created_by_user: str = Field(default=None)
    contact_name: str = Field(default=None)
    contact_number: str = Field(default=None)
    created_at: datetime = Field(default=None)
    last_updated_at: datetime = Field(default=None)
    emergency_type: EmergencyTypesEnum = Field(default=None)
    description: str = Field(default=None)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)
    x: float = Field(default=None)
    y: float = Field(default=None)
    location: str = Field(default=None)

    is_active: bool = Field(default=True)
    is_verified: bool = Field(default=False)
    
    verification_note: str = Field(default=None)

    # is_disproved: bool = Field(default=None)
    # note: str = Field(default=None)
    # dealt_time: datetime = Field(default=None)
    # dealt_by_user: str = Field(default=None)


class Emergencies(BaseModel):
    emergencies: List[Emergency]
    
class EmergencyKey(BaseModel):
    _id: str = Field(default=None)
    
class EmergencyKeys(BaseModel):
    emergencies: List[EmergencyKey]
