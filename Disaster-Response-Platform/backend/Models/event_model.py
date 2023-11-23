from pydantic import BaseModel, Field
from typing import List
from datetime import date, datetime

# - ID: UUID
# - eventType: Enum
# - eventTime: Datetime
# - endTime: DateTime
# - isActive: bool
# - eventCenterLocation: Location
# - eventMaxXDistance: int
# - eventMaxYDistance: int
#
# - creationTime: Datetime
# - createdByUser: String
#
# - confirmerUserName: String
# - lastConfirmed: DateTime
#
# - relatedActions: List<ActionIDs>
# - relatedNeeds: List<NeedIDs>

class EventKey(BaseModel):
    _id: str = Field(default=None)
class Event(BaseModel):
    _id: str = Field(default=None)
    event_type: str = Field(default=None)
    event_time: datetime = Field(default=None)
    end_time: datetime = Field(default=None)
    is_active: bool = Field(default=None)
    center_location_x: float = Field(default=None)
    center_location_y: float = Field(default=None)

    max_distance_x: float = Field(default=None)
    max_distance_y: float = Field(default=None)

    created_time: datetime = Field(default=None)
    created_by_user: str = Field(default=None)

    last_confirmed_time: datetime = Field(default=None)
    confirmed_by_user: str = Field(default=None)
    upvote: int = Field(default=0)
    downvote: int = Field(default=0)

class ActionRelations(BaseModel):
    action_id: str
    event_id: str

class NeedRelations(BaseModel):
    need_id: str
    event_id: str

class Events(BaseModel):
    events: List[Event]

class EventKeys(BaseModel):
    events: List[EventKey]
