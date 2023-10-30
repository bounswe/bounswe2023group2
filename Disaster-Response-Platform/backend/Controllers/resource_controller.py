import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends
from Models.resource_model import Resource
import Services.resource_service as resource_service
import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error

# Change history:
# 26/10/2023 The changes for auth2 authorization added and merged
# 30/10/2023 Add additional methods

router = APIRouter()

@router.post("/", status_code=201)
def create_resource(resource: Resource, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        resource.created_by = current_user
        resource_result = resource_service.create_resource(resource)
        response.status_code = HTTPStatus.OK
        return json.loads(resource_result)
    except ValueError as err:
        err_json = create_json_for_error("Resource create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/{resource_id}")
def get_resource(resource_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        resource = resource_service.get_resource_by_id(resource_id)
        response.status_code = HTTPStatus.OK
        return json.loads(resource)
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/")
def get_all_resources(response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        resources = resource_service.get_resources()
        response.status_code = HTTPStatus.OK
        return json.loads(resources)
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.put("/{resource_id}")
def update_resource(resource_id: str, resource: Resource, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
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
def delete_resource(resource_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        res_list = resource_service.delete_resource(resource_id)
        response.status_code=HTTPStatus.OK
        return json.loads(res_list)
    except ValueError as err:
        err_json = create_json_for_error("Resource update error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.put("/{resource_id}/initial_quantity")
def set_initial_quantity_of_resource(resource_id: str, quantity: int, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        resource_service.set_initial_quantity(resource_id, quantity)
        response.status_code = HTTPStatus.OK
        return {"message": f"Initial quantity of resource {resource_id} set to {quantity}"}
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/{resource_id}/initial_quantity")
def get_initial_quantity_of_resource(resource_id: str, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        quantity = resource_service.get_initial_quantity(resource_id)
        response.status_code = HTTPStatus.OK
        return {"initialQuantity": quantity}
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.put("/{resource_id}/current_quantity")
def set_current_quantity_of_resource(resource_id: str, quantity: int, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        resource_service.set_current_quantity(resource_id, quantity)
        response.status_code = HTTPStatus.OK
        return {"message": f"Current quantity of resource {resource_id} set to {quantity}"}
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/{resource_id}/current_quantity")
def get_current_quantity_of_resource(resource_id: str, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        quantity = resource_service.get_current_quantity(resource_id)
        response.status_code = HTTPStatus.OK
        return {"currentQuantity": quantity}
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.put("/{resource_id}/condition")
def set_condition_of_resource(resource_id: str, condition: ConditionEnum, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        resource_service.set_condition(resource_id, condition)
        response.status_code = HTTPStatus.OK
        return {"message": f"Condition of resource {resource_id} set to {condition}"}
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/{resource_id}/condition")
def get_condition_of_resource(resource_id: str, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        condition = resource_service.get_condition(resource_id)
        response.status_code = HTTPStatus.OK
        return {"condition": condition}
    except ValueError as err:
        err_json = create_json_for_error("Resource error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
