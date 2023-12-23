from fastapi import APIRouter, Response, Depends, status
from http import HTTPStatus
from Models.user_profile_model import UserOptionalInfo, UserOptionalInfos
from Models.user_model import Error
import Services.uprofile_optinfo_service as user_profile_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service

router = APIRouter()

@router.get("/user-optional-infos", responses={
    status.HTTP_200_OK: {"model": UserOptionalInfos},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
} )
async def get_user_optional_info(response: Response, anyuser: str = None, current_username: str = Depends(authentication_service.get_current_username)):
    if anyuser is None:
        username = current_username
    else:
        if (authentication_service.is_admin(current_username)):
            username = anyuser
        else:
            response.status_code = HTTPStatus.UNAUTHORIZED
            return create_json_for_error("Action prohibited", "Only users with ADMIN role can do that")

    try:
        json_result = user_profile_service.get_user_optional_info(username)
        response.status_code = HTTPStatus.OK
        return json.loads(json_result)
    except ValueError as val_error:
        response.status_code = HTTPStatus.NOT_FOUND
        json_result = create_json_for_error("Get user optional info faild", str(val_error))
        return json.loads(json_result)


@router.post("/user-optional-infos/add-user-optional-info", responses={
    status.HTTP_200_OK: {"model": UserOptionalInfo},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def set_user_optional_info(user_optional_info: UserOptionalInfo, response: Response, anyuser: str = None,
                                 current_username: str = Depends(authentication_service.get_current_username)):
    if anyuser is None:
        username = current_username
    else:
        if (authentication_service.is_admin(current_username)):
            username = anyuser
        else:
            response.status_code = HTTPStatus.UNAUTHORIZED
            return create_json_for_error("Action prohibited", "Only users with ADMIN role can do that")

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


@router.post("/delete-user-optional-info-item", )
async def set_user_optional_info(reset_field: str, response: Response, anyuser: str = None,
                                 current_username: str = Depends(authentication_service.get_current_username)):
    if anyuser is None:
        username = current_username
    else:
        if (authentication_service.is_admin(current_username)):
            username = anyuser
        else:
            response.status_code = HTTPStatus.UNAUTHORIZED
            return create_json_for_error("Action prohibited", "Only users with ADMIN role can do that")
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


@router.delete("/delete-user-optional-info", responses={
    status.HTTP_200_OK: {"model": UserOptionalInfo},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
} )
async def delete_user_optional_info(response: Response, anyuser: str = None, current_username: str = Depends(authentication_service.get_current_username)):
    if anyuser is None:
        username = current_username
    else:
        if (authentication_service.is_admin(current_username)):
            username = anyuser
        else:
            response.status_code = HTTPStatus.UNAUTHORIZED
            return create_json_for_error("Action prohibited", "Only users with ADMIN role can do that")

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
async def upload_user_profile_picture(response: Response, anyuser: str = None, current_username: str = Depends(authentication_service.get_current_username)):
    if anyuser is None:
        username = current_username
    else:
        if (authentication_service.is_admin(current_username)):
            username = anyuser
        else:
            response.status_code = HTTPStatus.UNAUTHORIZED
            return create_json_for_error("Action prohibited", "Only users with ADMIN role can do that")

    json_result = create_json_for_simple("Not ready API",
                                         "PICTUREID")
    return json.loads(json_result)