from fastapi import APIRouter, Response, Depends
from http import HTTPStatus
from Models.user_profile_model import *
import Services.uprofile_SocMed_service as uprofile_SocMed_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service



router = APIRouter()


@router.get("/socialmedia-links", )
async def get_user_socialmedia(response: Response, anyuser:str= None, platform_name:str = None, current_username: str = Depends(authentication_service.get_current_username)):
    if anyuser is None:
        if platform_name is None:
            username = current_username
        else:
            username = None
    else:
        username = anyuser

    try:
        result = uprofile_SocMed_service.get_user_social_media(username,platform_name)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        return create_json_for_error("User social media links not fetched", str(err))

@router.post("/socialmedia-links/add-socialmedia-link", )
async def add_a_socialmedia_currentuser(user_socialmedia:UserSocialMediaLink, response: Response, anyuser:str= None, current_username: str = Depends(authentication_service.get_current_username)):
    try:
        user_socialmedia.username = current_username
        result = uprofile_SocMed_service.add_user_socialmedia(user_socialmedia)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        err_json =  create_json_for_error("User profile not updated", str(err))
        return json.loads(err_json)

@router.delete("/socialmedia-links", )
async def delete_current_users_language(user_socialmedia:UserSocialMediaLink, response: Response, anyuser:str= None, current_username: str = Depends(authentication_service.get_current_username)):
    try:
        user_socialmedia.username = current_username
        result = uprofile_SocMed_service.delete_user_language(user_socialmedia)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        err_json = create_json_for_error("User social media link not fetched", str(err))
        return json.loads(err_json)
