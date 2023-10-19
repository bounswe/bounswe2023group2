from fastapi import APIRouter,Response
from http import HTTPStatus
from Services.authentication import *
from Services.build_API_returns import *
from Services.user_session import *
from pydantic import BaseModel

class User(BaseModel):
    username: str
    password: str

router = APIRouter()

# Sample space 1LyGBqAPMQYKN
@router.get("/authonticate", )
async def authenticate(username, password, response:Response):
    if (verify_user_password(username, password)):
        session_token =  create_session_token(username,password)
        json_result = create_json_for_simple(session_token, "SESSIONTOKEN")
        return json.loads(json_result)
    else:
        json_result = create_json_for_error("Login Failed", "Incorrect username or password" )
        response.status_code = HTTPStatus.UNAUTHORIZED
        return json.loads(json_result)

@router.post("/authonticate/create-user", )
async def create_user(user:User, response:Response):
    user_id = create_user_with_password(user.username,user.password)
    json_result = create_json_for_simple(user.username, "USERCREATED")
    return json.loads(json_result)

@router.post("/authonticate/update-password", )
async def update_password(user: User, response: Response):
    update_user_password(user.username,user.password)
    json_result = create_json_for_simple(user.username, "USERPASSWORDUPDATED")
    return json.loads(json_result)
