import json
from http import HTTPStatus
from fastapi import APIRouter, HTTPException, Response, Depends, Query
from Models.need_model import Need, QuantityUpdate,UrgencyUpdate
import Services.need_service as need_service
import Services.feedback_service as feedback_service
import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error
from typing import List, Optional

router = APIRouter()

@router.post("/", status_code=201)
def create_need(need: Need, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
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
def get_need(need_id: str, response:Response):
    try:
        need = need_service.get_need_by_id(need_id)
        response.status_code = HTTPStatus.OK
        return need
    except ValueError as err:
        err_json = create_json_for_error("Need error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)    
    

@router.get("/")
def get_all_needs(
    response: Response,
    active: Optional[bool] = Query(None, description="Filter by active status"),
    types: List[str] = Query(None, description="Filter by types of needs"),
    subtypes: List[str] = Query(None, description="Filter by subtypes of needs"),
    x: float = Query(None, description="X coordinate for distance calculation"),
    y: float = Query(None, description="Y coordinate for distance calculation"),
    distance_max: float = Query(None, description="Maximum distance for filtering"),
    sort_by: str = Query('created_at', description="Field to sort by"),
    order: Optional[str] = Query('desc', description="Sort order")
):

    if types:
        types_list = types[0].split(',')
    else:
        types_list = []

    if subtypes:
        subtypes_list = subtypes[0].split(',')
    else:
        subtypes_list = []

    try:
        needs = need_service.get_needs(
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
        return json.loads(needs)
    except ValueError as err:
        err_json = create_json_for_error("Need error", str(err))
        response.status_id = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.put("/{need_id}")
def update_need(need_id: str, need: Need, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    
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
def delete_need(need_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        res = need_service.delete_need(need_id)
        response.status_code=HTTPStatus.OK
        return json.loads(res)
        
    except ValueError as err:
        err_json = create_json_for_error("Need delete error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)    
    

@router.put("/{need_id}/initial_quantity")
def set_initial_quantity(need_id: str, quantity_data: QuantityUpdate, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        need_service.set_initial_quantity(need_id, quantity_data.quantity)
        response.status_code = HTTPStatus.OK
        return {"message": f"Initial quantity of need {need_id} is set to {quantity_data.quantity}"}
    except ValueError as err:
        err_json = create_json_for_error("Need update error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
@router.get("/{need_id}/initial_quantity")
def get_initial_quantity(need_id: str, response: Response):
    try:
        quantity = need_service.get_initial_quantity(need_id)
        response.status_code = HTTPStatus.OK
        return {"Initial quantity": quantity}
    except ValueError as err:
        err_json = create_json_for_error("Need initial quantity error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)


@router.put("/{need_id}/unsupplied_quantity")
def set_unsupplied_quantity(need_id: str, quantity_data: QuantityUpdate, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        need_service.set_unsupplied_quantity(need_id, quantity_data.quantity)
        response.status_code = HTTPStatus.OK
        return {"message": f"Unsupplied quantity of need {need_id} is set to {quantity_data.quantity}"}
    except ValueError as err:
        err_json = create_json_for_error("Need unsupplied quantity set update error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
@router.get("/{need_id}/unsupplied_quantity")
def get_unsupplied_quantity(need_id: str, response: Response):
    try:
        quantity = need_service.get_unsupplied_quantity(need_id)
        response.status_code = HTTPStatus.OK
        return {"Unsupplied quantity": quantity}
    except ValueError as err:
        err_json = create_json_for_error("Need unsupplied quantity get error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
@router.put("/{need_id}/urgency")
def set_urgency(need_id: str, urgency_data: UrgencyUpdate, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        need_service.set_urgency(need_id, urgency_data.urgency)
        response.status_code = HTTPStatus.OK
        return {"message": f"Urgency of need {need_id} is set to {urgency_data.urgency}"}
    except ValueError as err:
        err_json = create_json_for_error("Need urgency set update error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/{need_id}/urgency")
def get_urgency(need_id: str, response: Response):
    try:
        urgency = need_service.get_urgency(need_id)
        response.status_code = HTTPStatus.OK
        return {"Urgency": urgency}
    except ValueError as err:
        err_json = create_json_for_error("Need urgency get error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)