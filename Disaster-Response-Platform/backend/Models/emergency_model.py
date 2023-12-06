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
    emergency_type: EmergencyTypesEnum = Field(default=None)
    # center_location_x: float = Field(default=None)
    # center_location_y: float = Field(default=None)
    #
    # max_distance_x: float = Field(default=None)
    # max_distance_y: float = Field(default=None)

    x: float = Field(default=None)
    y: float = Field(default=None)

    emergency_contact_name: str = Field(default=None)
    emergency_contact_number: str = Field(default=None)

    note: str = Field(default=None)
    short_description: str = Field(default=None)

    is_active: bool = Field(default=None)
    is_verified: bool = Field(default=None)
    is_disproved: bool = Field(default=None)

    upvote: int = Field(default=0)
    downvote: int = Field(default=0)

    created_time: datetime = Field(default=None)
    created_by_user: str = Field(default=None)

    dealt_time: datetime = Field(default=None)
    dealt_by_user: str = Field(default=None)

    verification_note: str = Field(default=None)

    last_updated_time: datetime = Field(default=None)


class Emergencies(BaseModel):
    events: List[Emergency]
