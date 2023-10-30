import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends
from Models.need_model import Need
import Services.need_service as need_service

import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error

router = APIRouter()

@router.post("/", status_code=201)
def create_need(need: Need, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        need.created_by = current_user
        need_result = need_service.create_need(need)
        response.status_code = HTTPStatus.OK
        return json.loads(need_result)
    except ValueError as err:
        err_json = create_json_for_error("Need create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/{need_id}")
def get_need(need_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        need = need_service.get_need_by_id(need_id)
        response.status_code = HTTPStatus.OK
        return json.loads(need)
    except ValueError as err:
        err_json = create_json_for_error("Need error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)    
    

@router.get("/")
def get_all_needs(response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        needs = need_service.get_needs()
        response.status_code = HTTPStatus.OK
        return json.loads(needs)
    except ValueError as err:
        err_json = create_json_for_error("Need error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)  

@router.put("/{need_id}")
def update_need(need_id: str, need: Need, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    
    try:
        updated_need = need_service.update_need(need_id, need)
    
        if updated_need:
            response.status_code = HTTPStatus.OK
            return {"needs": [updated_need]}
        else:
            raise ValueError(f"Need id {need_id} not updated")
    except ValueError as err:
        err_json = create_json_for_error("Need error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    

    
@router.delete("/{need_id}")
def delete_need(need_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        need_service.delete_need(need_id)
        response.status_code=HTTPStatus.NO_CONTENT
    except ValueError as err:
        err_json = create_json_for_error("Need update error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)    
    
