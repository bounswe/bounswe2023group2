import http

from fastapi import APIRouter, Response, Depends, status
from typing import List
from http import HTTPStatus
from Models.user_profile_model import *
from Models.user_model import Error
import Services.uprofile_professions_service as uprofile_professions_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service



router = APIRouter()

@router.get("/professions", responses={
    status.HTTP_200_OK: {"model": Professions},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def get_user_profession_level(response: Response, anyuser:str= None, profession:str = None, current_username: str = Depends(authentication_service.get_current_username)):
    if anyuser is None:
        if profession is None:
            username = current_username
        else:
            username = None
    else:
        username = anyuser

    try:
        result = uprofile_professions_service.get_user_profession(username, profession)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        return create_json_for_error("User profession not fetched", str(err))


@router.post("/professions/add-profession", responses= {status.HTTP_200_OK: {"model": Professions},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def add_a_language_currentuser(user_profession: UserProfession, response: Response, anyuser:str= None, current_username: str = Depends(authentication_service.get_current_username)):
    try:
        user_profession.username = current_username
        result = uprofile_professions_service.add_user_profession(user_profession)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        err_json =  create_json_for_error("User profession not updated", str(err))
        return json.loads(err_json)

@router.post("/professions", responses= {status.HTTP_200_OK: {"model": UserProfession},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def delete_current_users_language(user_profession: UserProfession, response: Response, anyuser:str= None, current_username: str = Depends(authentication_service.get_current_username)):

    try:
        user_profession.username = current_username
        result = uprofile_professions_service.delete_user_profession(user_profession)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        err_json =  create_json_for_error("User profession not fetched", str(err))
        return json.loads(err_json)
