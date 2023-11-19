import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends, Body
from starlette import status

from Models.event_model import Event, NeedRelations, ActionRelations, Events, EventKeys
from Models.user_model import Error
import Services.event_service as event_service
import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error

# Change history:
# Created using resource as a template

router = APIRouter()

@router.post("/", responses={
    status.HTTP_200_OK: {"model": Events},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}})
def create_event(event:Event, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        event.created_by_user = current_user
        event_result = event_service.create_event(event)
        response.status_code = HTTPStatus.OK
        return json.loads(event_result)
    except ValueError as err:
        err_json = create_json_for_error("Event create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

# Get the resource with the specified ID.
@router.get("/{event_id}", responses={
    status.HTTP_200_OK: {"model": Events},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
def get_event(event_id: str, response: Response):
    try:
        event = event_service.get_event_by_id(event_id)
        response.status_code = HTTPStatus.OK
        return json.loads(event)
    except ValueError as err:
        err_json = create_json_for_error("Event error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

# Get all events.
@router.get("/", responses={
    status.HTTP_200_OK: {"model": Events},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}})
def get_all_events(response: Response):
    try:
        events = event_service.get_events()
        response.status_code = HTTPStatus.OK
        return json.loads(events)
    except ValueError as err:
        err_json = create_json_for_error("Event error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)


@router.patch("/{event_id}", responses={
    status.HTTP_200_OK: {"model": Events},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}})
def update_event(event_id: str, event:Event, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        updated_event = event_service.update_event(event_id, event)

        if updated_event:
            response.status_code = HTTPStatus.OK
            return {"events": [updated_event]}
        else:
            raise ValueError(f"Event id {event_id} not updated")
    except ValueError as err:
        err_json = create_json_for_error("Event error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
# Response Body = {"quantity": 75}
@router.delete("/{event_id}", responses={
    status.HTTP_200_OK: {"model": EventKeys},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}})
def delete_event(event_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        event_list = event_service.delete_event(event_id)
        response.status_code=HTTPStatus.OK
        return json.loads(event_list)
    except ValueError as err:
        err_json = create_json_for_error("Event delete error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)