from fastapi import APIRouter, Response, Depends, status
from http import HTTPStatus
from Models.user_profile_model import *
from Models.user_model import Error
import Services.uprofile_skills_service as uprofile_skills_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service

router = APIRouter()

@router.get("/skills", responses={
    status.HTTP_200_OK: {"model": UserSkills},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def get_user_skill_level(response: Response, anyuser:str= None, skill:str = None,
                               current_username: str = Depends(authentication_service.get_current_username)):
    if anyuser is None:
        if skill is None:
            username = current_username
        else:
            username = None
    else:
        username = anyuser

    try:
        result = uprofile_skills_service.get_user_skill(username, skill)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        return create_json_for_error("User skill not fetched", str(err))


@router.post("/skills/add-skill", responses={
    status.HTTP_200_OK: {"model": UserSkills},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def add_a_language_currentuser(user_skill: UserSkill, response: Response, current_username: str = Depends(authentication_service.get_current_username)):
    try:
        user_skill.username = current_username
        result = uprofile_skills_service.add_user_skill(user_skill)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        err_json =  create_json_for_error("User skill not updated", str(err))
        return json.loads(err_json)

@router.post("/skills", responses={
    status.HTTP_200_OK: {"model": UserSkills},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def delete_current_users_language(user_skill: UserSkill, response: Response, current_username: str = Depends(authentication_service.get_current_username)):

    try:
        user_skill.username = current_username
        result = uprofile_skills_service.delete_user_skill(user_skill)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        err_json =  create_json_for_error("User skill not fetched", str(err))
        return json.loads(err_json)
