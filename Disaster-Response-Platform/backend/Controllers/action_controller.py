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
        response.status_code = HTTPStatus.CREATED
        return action_result
    except ValueError as err:
        error= Error(ErrorMessage="Action could not be created", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error
    
@router.put("/do/{action_id}", responses={
    status.HTTP_201_CREATED: {"model": doActionResponse},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
def do_action(action_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        action_result = action_service.do_action(action_id, current_user)
        response.status_code = HTTPStatus.OK
        return action_result
    except ValueError as err:
        error= Error(ErrorMessage="Action could not be done", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error
    

@router.get("/group",responses={
    status.HTTP_200_OK: {"model": ActionSuccess},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
def get_group_info(related_group: ActionGroup , response: Response):
    try:
        resource_text = action_service.get_group_info(related_group)
        response.status_code = HTTPStatus.OK
        return resource_text
    except ValueError as err:
        error= Error(ErrorMessage="Group check fails", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error

@router.get("/{action_id}",responses={
    status.HTTP_200_OK: {"model": Action},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
def get_action_by_id(action_id: str , response: Response):
    try:
        action = action_service.get_action(action_id)
        response.status_code = HTTPStatus.OK
        return action
    except ValueError as err:
        error= Error(ErrorMessage="Action get error", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error

    
@router.get("/",responses={
    status.HTTP_200_OK: {"model": ActionSuccess},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
def get_all_actions(response: Response):
    try:
        actions = action_service.get_actions()
        response.status_code = HTTPStatus.OK
        return json.loads(actions)
    except ValueError as err:
        error= Error(ErrorMessage="Action error", ErrorDetail= str(err))
        response.status_code= HTTPStatus.BAD_REQUEST
        response.response_model= Error
        return error
    

@router.put("/{action_id}", responses={
    status.HTTP_201_CREATED: {"model": updateResponse},
    status.HTTP_400_BAD_REQUEST: {"model": Error}
})
def update_action(action_id: str,action: Action, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        action.created_by= current_user
        updated_action = action_service.update_action(action_id, action)
        if updated_action:
            response.status_code = HTTPStatus.OK
            return {"actions": [updated_action]}
        else:
            raise ValueError(f"Action id {action_id} not updated")
    except ValueError as err:
        err_json = create_json_for_error("Action error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
@router.delete("/{action_id}")
def delete_action(action_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        res_list = action_service.delete_action(action_id, current_user)
        response.status_code=HTTPStatus.OK
        return json.loads(res_list)
    except ValueError as err:
        err_json = create_json_for_error("Action could not be deleted", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

#this is for making an action inactive 
@router.delete("/{action_id}")
def cancel_action(action_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        res_list = action_service.delete_action(action_id, current_user)
        response.status_code=HTTPStatus.OK
        return json.loads(res_list)
    except ValueError as err:
        err_json = create_json_for_error("Action could not be cancelled", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)