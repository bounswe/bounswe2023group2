import json
from http import HTTPStatus
from fastapi import APIRouter, HTTPException, Response, Depends, Query
from Models.user_model import Error
from Models.emergency_model import Emergency, Emergencies, EmergencyKeys 
import Services.emergency_service as emergency_service
# import Services.feedback_service as feedback_service
import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error
from typing import List, Optional
from Models.user_model import UserProfile
from starlette import status


router = APIRouter()

@router.post("/", responses={
    status.HTTP_200_OK: {"model": Emergencies},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}})
def create_emergency(emergency: Emergency, response:Response, user: UserProfile = authentication_service.get_current_user):
    try:
        if user is None:
            emergency.created_by_user = "ANONYMOUS"
        else:
            emergency.created_by_user = user.username
        
        emergency_result = emergency_service.create_emergency(emergency)
        response.status_code = HTTPStatus.OK
        return json.loads(emergency_result)
    except ValueError as err:
        err_json = create_json_for_error("Emergency create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

# Get the emergency with the specified ID.
@router.get("/{emergency_id}", responses={
    status.HTTP_200_OK: {"model": Emergencies},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})
def get_emergency(emergency_id: str, response: Response):
    try:
        emergency = emergency_service.get_emergency_by_id(emergency_id)
        response.status_code = HTTPStatus.OK
        return json.loads(emergency)
    except ValueError as err:
        err_json = create_json_for_error("Emergency error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

# Get all emergencies.
@router.get("/", responses={
    status.HTTP_200_OK: {"model": Emergencies},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}})
def get_all_emergencies(
    response: Response,
    emergency_types: List[str] = Query(None, description="Filter by types of emergencies"),
    contact_names: List[str] = Query(None, description="Filter by Contact Names"),
    is_active: Optional[bool] = Query(None, description="Filter by active status"),
    is_verified: Optional[bool] = Query(None, description="Filter by verification status"),
    x: float = Query(None, description="X coordinate for distance calculation"),
    y: float = Query(None, description="Y coordinate for distance calculation"),
    distance_max: float = Query(None, description="Maximum distance for filtering"),
    sort_by: str = Query('created_at', description="Field to sort by"),
    order: Optional[str] = Query('desc', description="Sort order")
):
    if emergency_types:
        types_list = emergency_types[0].split(',')
    else:
        types_list = []

    try:
        emergencies = emergency_service.get_emergencies(
            emergency_types=types_list,
            is_active=is_active,
            is_verified=is_verified,
            x=x,
            y=y,
            distance_max=distance_max,
            sort_by=sort_by,
            order=order,
            contact_names=contact_names
        )
        response.status_code = HTTPStatus.OK
        return json.loads(emergencies)
    except ValueError as err:
        err_json = create_json_for_error("Emergency error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)


@router.patch("/{emergency_id}", responses={
    status.HTTP_200_OK: {"model": Emergencies},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}})
def update_emergency(emergency_id: str, emergency:Emergency, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        updated_emergency = emergency_service.update_emergency(emergency_id, emergency)

        if updated_emergency:
            response.status_code = HTTPStatus.OK
            return json.loads(updated_emergency)
        else:
            raise ValueError(f"Emergency id {emergency_id} not updated")
    except ValueError as err:
        err_json = create_json_for_error("Emergency error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
# Response Body = {"quantity": 75}
@router.delete("/{emergency_id}", responses={
    status.HTTP_200_OK: {"model": EmergencyKeys},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}})
def delete_emergency(emergency_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        emergency_list = emergency_service.delete_emergency(emergency_id)
        response.status_code=HTTPStatus.OK
        return json.loads(emergency_list)
    except ValueError as err:
        err_json = create_json_for_error("Emergency delete error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)