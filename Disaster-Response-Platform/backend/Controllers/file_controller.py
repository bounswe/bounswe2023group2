from fastapi import FastAPI, UploadFile, Response, status, Depends
from http import HTTPStatus
from fastapi import APIRouter
from Database.s3 import upload_file_using_client
from Models.user_model import Error
from Services import authentication_service
router = APIRouter() 

@router.post("/uploadfile/", responses ={
    status.HTTP_200_OK: {"model": str},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
async def create_upload_file(file: UploadFile, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        object_name = file.filename.replace(" ", "_")
        f = open(f'./tmp/{object_name}', 'w')
        f.write(file.file.read().decode('utf-8'))
        f.close()

        result = upload_file_using_client(object_name)
        return {"url": result }
    except ValueError as err:
        error= Error(ErrorMessage="UPLOAD_IMAGE_ERROR", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error
   
   
