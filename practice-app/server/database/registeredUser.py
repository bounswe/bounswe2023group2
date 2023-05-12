from pydantic import BaseModel

class RegisteredUser(BaseModel):
    username: str
    first_name: str
    last_name: str
    email: str
    phone_number: str
    password : str