from pydantic import BaseModel
from typing import Optional

# async def add_resource(type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, condition, quantity):
class Resource(BaseModel):
    type: str
    location: str
    notes: Optional[str]
    updated_at: str
    is_active: bool
    upvotes: int
    downvotes: int
    creator_id: str
    creation_date: str
    condition: str
    quantity: int

# async def add_need(type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, urgency, quantity):
class Need(BaseModel):
    type: str
    location: str
    notes: Optional[str]
    updated_at: str
    is_active: bool
    upvotes: int
    downvotes: int
    creator_id: str
    creation_date: str
    urgency: str
    quantity: int

# async def add_event(type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, duration):
class Event(BaseModel):
    type: str
    location: str
    notes: Optional[str]
    updated_at: str
    is_active: bool
    upvotes: int
    downvotes: int
    creator_id: str
    creation_date: str
    duration: int

# async def add_action(notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, start_location, end_location, status):
class Action(BaseModel):
    notes: Optional[str]
    updated_at: str
    is_active: bool
    upvotes: int
    downvotes: int
    creator_id: str
    creation_date: str
    start_location: str
    end_location: str
    status: str
    used_resources: Optional[str]
    created_resources: Optional[str]
    fulfilled_needs: Optional[str]
    emerged_needs: Optional[str]
    related_events: Optional[str]
