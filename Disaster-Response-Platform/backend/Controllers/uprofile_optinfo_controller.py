from fastapi import APIRouter, Response, Depends
from http import HTTPStatus
from Models.user_profile_model import UserOptionalInfo
import Services.uprofile_optinfo_service as user_profile_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service

router = APIRouter()

def general_user_optional_info(response, username:str) ->json:
    try:
        json_result = user_profile_service.get_user_optional_info(username)
        response.status_code = HTTPStatus.OK
        return json.loads(json_result)
    except ValueError as val_error:
        response.status_code = HTTPStatus.NOT_FOUND
        json_result = create_json_for_error("Get user optional info faild", str(val_error))
        return json.loads(json_result)

@router.get("/get-user-optional-info", )
async def get_user_optional_info(response: Response, username: str = Depends(authentication_service.get_current_username)):
    return general_user_optional_info(response, username)


@router.get("/all-user-optional-infos", )
async def get_all_user_optional_info(response: Response, username: str = Depends(authentication_service.get_current_username)):
    return general_user_optional_info(response=response)


@router.post("/set-user-optional-info", )
async def set_user_optional_info(user_optional_info: UserOptionalInfo, response: Response, username: str = Depends(authentication_service.get_current_username)):
    try:
        user_optional_info.username  = username
        result = user_profile_service.set_user_optional_info(user_optional_info)
        json_result = json.loads(result)
        response.status_code = HTTPStatus.OK
        return json_result
    except ValueError as val_error:
        response.status_code = HTTPStatus.NOT_FOUND
        json_result = create_json_for_error("Optional user info set failed", str(val_error))
        return json.loads(json_result)


@router.post("/reset-user-optional-info", )
async def set_user_optional_info(reset_field: str, response: Response, username: str = Depends(authentication_service.get_current_username)):
    try:
        user_profile_service.reset_user_optional_info(username, reset_field)
        response.status_code = HTTPStatus.OK
        json_result = create_json_for_simple(f"Optional Info field {reset_field} reset for user  {username}",
                                             "USERUPDATED")
        return json.loads(json_result)
    except ValueError as val_error:
        response.status_code = HTTPStatus.NOT_FOUND
        json_result = create_json_for_error("Optional user info reset failed", str(val_error))
        return json.loads(json_result)


@router.delete("/delete-user-optional-info", )
async def delete_user_optional_info(response: Response, username: str = Depends(authentication_service.get_current_username)):
    try:
        json_result = user_profile_service.delete_user_optional_info(username)
        response.status_code = HTTPStatus.OK
        return json.loads(json_result)
    except ValueError as val_error:
        response.status_code = HTTPStatus.NOT_FOUND
        json_result = create_json_for_error("Optional user info delete failed", str(val_error))
        return json.loads(json_result)

# Will be written seperately - the model will not include BSON
@router.post("/upload-user-profile-picture", )
async def upload_user_profile_picture(response: Response, username: str = Depends(authentication_service.get_current_username)):
    json_result = create_json_for_simple("Not ready API",
                                         "PICTUREID")
    return json.loads(json_result)