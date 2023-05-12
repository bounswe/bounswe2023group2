from pydantic import BaseModel

class LoginUser(BaseModel): #this is a model for get data from frontend to api easily, it's not in database it's only used for this aim.
    username: str
    password : str