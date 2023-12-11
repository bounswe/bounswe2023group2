from pydantic import BaseModel, Field
import datetime

# Function to get current time in GMT+3
def current_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3)

 # Function to get expiration time in GMT+3, 10 minutes from now
def expiration_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3) + datetime.timedelta(minutes=10)   

class EmailVerification(BaseModel):
    username: str = Field(default=None)
    email: str = Field(default=None)
    token: str = Field(default=None)
    expiration: datetime.datetime = Field(default_factory=expiration_time_gmt3) # expires in 10 min 

