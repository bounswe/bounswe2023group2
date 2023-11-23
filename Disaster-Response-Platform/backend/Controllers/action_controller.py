import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends, Body,status, BackgroundTasks
from Models.resource_model import Resource, ConditionEnum, QuantityUpdate, ConditionUpdate
from Services import action_service
import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error
from Models.action_model import *
from Models.user_model import Error
from datetime import datetime, timedelta

router = APIRouter()

@router.post("/", responses={
    status.HTTP_201_CREATED: {"model": ActionSuccess},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
def create_action(action: Action, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        action.created_by = current_user
        action_result = action_service.create_action(action)
        response.status_code = HTTPStatus.OK
        return json.loads(action_result)
    except ValueError as err:
        error= Error(ErrorMessage="Action could not be created", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error
    
