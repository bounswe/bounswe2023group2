import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends, Body,status, BackgroundTasks
from Models.resource_model import Resource, ConditionEnum, QuantityUpdate, ConditionUpdate
from Services import action_service_v2
import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error
from Models.action_model_v2 import *
from Models.user_model import Error
from datetime import datetime, timedelta

router = APIRouter()
#TODO as soon as do_action

@router.get("/")
def getAll( response: Response):
    try:
        action_list = action_service_v2.find_many()
        response.status_code = HTTPStatus.OK
        return json.loads(action_list)
    except ValueError as err:
        error= Error(ErrorMessage="Action could not be fetched", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error

@router.get("/{id}")
def get(id:str, response: Response):
    try:
        action = action_service_v2.get_action(id)
        response.status_code = HTTPStatus.OK
        return json.loads(action)
    except ValueError as err:
        error= Error(ErrorMessage="Action could not be fetched", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error

@router.post("/", responses={
    status.HTTP_201_CREATED: {"model": ActionSuccess},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
def create_action(action: Action, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        action.created_by = current_user
        action_result = action_service_v2.create_action(action)
        response.status_code = HTTPStatus.CREATED
        return action_result
    except ValueError as err:
        error= Error(ErrorMessage="Action could not be created", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error


@router.post("/{action_id}")
def update(action_id:str, body: Action, response: Response ,current_user: str = Depends(authentication_service.get_current_username)):
    try:      
        updated = action_service_v2.update(action_id, current_user, body)
        response.status_code = HTTPStatus.OK
        return {updated}
    except ValueError as err:
        error= Error(ErrorMessage="Action could not be updated", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error


@router.get("/need_list/{id}")
def get_related_needs(id: str, response: Response):
    try:
        response.status_code = HTTPStatus.OK
        need_list = action_service_v2.get_related_needs(id)
        return json.loads(need_list)
    except ValueError as err:
        error= Error(ErrorMessage="Action could not be created", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error

@router.get("/resource_list/{id}")
def get_related_resources(id: str, response: Response):
    try:
        response.status_code = HTTPStatus.OK
        resource_list = action_service_v2.get_related_resources(id)
        return json.loads(resource_list)
    except ValueError as err:
        error= Error(ErrorMessage="Resources could not be fetched", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error

@router.get('/perform_action/{id}')
def perform_action(id: str, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        response.status_code = HTTPStatus.OK
        resource_list = action_service_v2.get_match_list(id,current_user)
        return resource_list
    except ValueError as err:
        error= Error(ErrorMessage="Resources could not be fetched", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error
@router.post('/perform_action/{id}')
def perform_action(id: str, match: MatchList, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        response.status_code = HTTPStatus.OK
        resource_list = action_service_v2.do_action(id,current_user, match)
        if not resource_list:
            return {"Message": "Moved", "Action_id":id}
        return json.loads(resource_list)
    except ValueError as err:
        error= Error(ErrorMessage="Resources could not be fetched", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error