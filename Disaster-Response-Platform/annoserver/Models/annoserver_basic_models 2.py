from pydantic import BaseModel

class Error(BaseModel):
    ErrorMessage: str
    ErrorDetail: str