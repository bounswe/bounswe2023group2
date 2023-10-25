import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends
from Models.resource_model import Resource
import Services.resource_service as resource_service
import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error

router = APIRouter()

@router.post("/", status_code=201)
def create_resource(resource: Resource, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        resource_result = resource_service.create_resource(resource)
        response.status_code = HTTPStatus.OK
        return json.loads(resource_result)
    except ValueError as err:
        err_json = create_json_for_error("Resource create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/{resource_id}")
def get_resource(resource_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        resource = resource_service.get_resource_by_id(resource_id)
        response.status_code = HTTPStatus.OK
        return json.loads(resource)
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/")
def get_all_resources(response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        resources = resource_service.get_resources()
        response.status_code = HTTPStatus.OK
        return json.loads(resources)
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.put("/{resource_id}")
def update_resource(resource_id: str, resource: Resource, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        updated_resource = resource_service.update_resource(resource_id, resource)
        if updated_resource:
            response.status_code = HTTPStatus.OK
            return {"resources": [updated_resource]}
        else:
            raise ValueError(f"Resource id {resource_id} not updated")
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)


    
@router.delete("/{resource_id}")
def delete_resource(resource_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    try:
        resource_service.delete_resource(resource_id)
        response.status_code=HTTPStatus.NO_CONTENT
    except ValueError as err:
        err_json = create_json_for_error("Resource update error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
