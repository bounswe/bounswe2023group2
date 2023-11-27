from fastapi import APIRouter, Response, Depends, status
from http import HTTPStatus
from Models.user_profile_model import *
from Models.user_model import Error
import Services.uprofile_optinfo_service as user_profile_service
import Services.uprofile_languages_service as uprofile_languages_service
import Services.uprofile_skills_service as uprofile_skills_service
import Services.uprofile_professions_service as uprofile_professions_service
import Services.uprofile_SocMed_service as uprofile_SocMed_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service

router = APIRouter()

@router.get("/user-profile", responses={
    status.HTTP_200_OK: {"model": UserProfileComplete},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
} )
async def get_complete_user_profile( anyuser: str, response: Response,  username: str = Depends(authentication_service.get_current_admin_user)):
    if (anyuser is None):
        anyuser = username
    json_opt_info = user_profile_service.get_user_optional_info(anyuser)
    json_languages = uprofile_languages_service.get_user_language(anyuser)
    json_professions = uprofile_professions_service.get_user_profession(anyuser)
    json_user_skills = uprofile_skills_service.get_user_skill(anyuser)
    json_socmed = uprofile_SocMed_service.get_user_social_media(anyuser)


    return { "userprofiles": [(json.loads(json_languages) | json.loads(json_opt_info) | json.loads(json_professions)
            | json.loads(json_user_skills) | json.loads(json_socmed))]}

