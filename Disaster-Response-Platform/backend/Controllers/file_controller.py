from fastapi import FastAPI, UploadFile, Response, status, Depends
from http import HTTPStatus
from Database.s3 import upload_file_using_client, delete_file_from_s3, create_and_upload_file
from Models.user_model import Error
from Services import authentication_service
from fastapi import Query, APIRouter, HTTPException, Response, Depends, Body
from typing import List, Optional
router = APIRouter() 

@router.post("/uploadfile/", responses ={
    status.HTTP_200_OK: {"model": str},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
async def create_upload_file(file: UploadFile, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        object_name = file.filename.replace(" ", "_")
        f = open(f'./tmp/{object_name}', 'wb')
        tmp = file.file.read()
        f.write(tmp)
        f.close()

        result = upload_file_using_client(object_name)
        return {"url": result }
    except ValueError as err:
        error= Error(ErrorMessage="UPLOAD_IMAGE_ERROR", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error

@router.post("/downloadfile/", responses ={
    status.HTTP_200_OK: {"model": str},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
async def create_download_file( 
    response:Response, 
    active: Optional[bool] = Query(None, description="Filter by active status"),
    types: List[str] = Query(None, description="Filter by types of resources"),
    subtypes: List[str] = Query(None, description="Filter by subtypes of resources"),
    x: float = Query(None, description="X coordinate for distance calculation"),
    y: float = Query(None, description="Y coordinate for distance calculation"),
    distance_max: float = Query(None, description="Maximum distance for filtering"),
    sort_by: str = Query('created_at', description="Field to sort by"),
    order: Optional[str] = Query('desc', description="Sort order")):
    if types:
        types_list = types[0].split(',')
    else:
        types_list = []

    if subtypes:
        subtypes_list = subtypes[0].split(',')
    else:
        subtypes_list = []
    try:
        result = create_and_upload_file(
            active=active,
            types=types_list,
            subtypes=subtypes_list,
            x=x,
            y=y,
            distance_max=distance_max,
            sort_by=sort_by,
            order=order
        )
        response.status_code = HTTPStatus.OK
        return {"url": result }
    except ValueError as err:
        error= Error(ErrorMessage="DOWNLOAD_IMAGE_ERROR", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error
   
   
@router.delete("/deletefile/{file_name}", responses ={
    HTTPStatus.OK: {"model": str},
    HTTPStatus.BAD_REQUEST: {"model": Error}
})
async def delete_file(file_name: str, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        # Call the function to delete the file from S3
        result = delete_file_from_s3(file_name)
        return {"message": result["message"]}
    except Exception as e:
        # Handle exceptions and return an error response
        error = Error(ErrorMessage="S3_DELETE_ERROR", ErrorDetail=str(e))
        response.status_code = HTTPStatus.BAD_REQUEST
        response.response_model = Error
        return error