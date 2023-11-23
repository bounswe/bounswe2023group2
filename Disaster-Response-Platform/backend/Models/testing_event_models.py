from pydantic import BaseModel, Field

class EventApi(BaseModel):
    base_url:str = "/api/events"
    prefix:str =  Field(default=None)
    name:str = Field(default=None)

