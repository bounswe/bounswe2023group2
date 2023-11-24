from fastapi import APIRouter, Response, Depends, status, UploadFile
from http import HTTPStatus
from Models.user_profile_model import UserOptionalInfo, UserOptionalInfos
from Models.user_model import Error
import Services.uprofile_optinfo_service as user_profile_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service
import os
from Database.s3 import upload_file_using_client
import shutil
import uuid

router = APIRouter()

def general_user_optional_info(response, username:str = None) ->json:
    try:
        json_result = user_profile_service.get_user_optional_info(username)
        response.status_code = HTTPStatus.OK
        return json.loads(json_result)
    except ValueError as val_error:
        response.status_code = HTTPStatus.NOT_FOUND
        json_result = create_json_for_error("Get user optional info faild", str(val_error))
        return json.loads(json_result)

@router.get("/user-optional-infos", responses={
    status.HTTP_200_OK: {"model": UserOptionalInfos},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
} )
async def get_user_optional_info(response: Response, username: str = Depends(authentication_service.get_current_username)):
    return general_user_optional_info(response, username)


@router.get("/all-user-optional-infos", responses={
    status.HTTP_200_OK: {"model": UserOptionalInfos},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def get_all_user_optional_info(response: Response, username: str = Depends(authentication_service.get_current_username)):
    return general_user_optional_info(response=response)


@router.post("/user-optional-infos/add-user-optional-info", responses={
    status.HTTP_200_OK: {"model": UserOptionalInfo},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
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


@router.post("/delete-user-optional-info-item", )
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


@router.delete("/delete-user-optional-info", responses={
    status.HTTP_200_OK: {"model": UserOptionalInfo},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
} )
async def delete_user_optional_info(response: Response, username: str = Depends(authentication_service.get_current_username)):
    try:
        json_result = user_profile_service.delete_user_optional_info(username)
        response.status_code = HTTPStatus.OK
        return json.loads(json_result)
    except ValueError as val_error:
        response.status_code = HTTPStatus.NOT_FOUND
        json_result = create_json_for_error("Optional user info delete failed", str(val_error))
        return json.loads(json_result)



@router.post("/upload-user-picture/")
async def create_upload_file(file: UploadFile, response:Response, username: str = Depends(authentication_service.get_current_username)):
    upload_dir = os.path.join(os.getcwd(), "tmpupload")
    # Create the upload directory if it doesn't exist
    if not os.path.exists(upload_dir):
        os.makedirs(upload_dir)

    local_file_name = str(uuid.uuid4())
    # get the destination path

    dest = os.path.join(upload_dir, local_file_name)
    print(dest)

    # copy the file contents
    with open(dest, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    json_result = user_profile_service.upload_user_picture(username, upload_dir, local_file_name)
    return json.loads(json_result)