from fastapi import APIRouter, HTTPException, Response, Depends, status
from Database.mongo import MongoDB
from Models.user_model import *
from http import HTTPStatus
from Services.user_roles_service import *
from Services import authentication_service

router = APIRouter()
db = MongoDB.getInstance()

userDb = MongoDB.get_collection('authenticated_user')
roleRequestsDb = MongoDB.get_collection('role_verification_requests')


@router.post("/proficiency-request", responses={
    status.HTTP_201_CREATED: {"model": ProfReqSuccess},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
async def proficiency_request(prof_request : ProfRequest, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        print("do you print this")
        prof_request.username= current_user
        result= create_proficiency_request(prof_request)
        response.status_code=status.HTTP_201_CREATED
        return result
    except ValueError as err:
        error= Error(ErrorMessage="Could not send proficiency verification request", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error

        return error