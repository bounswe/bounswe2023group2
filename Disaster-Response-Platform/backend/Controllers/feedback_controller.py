import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends
from Models.feedback_model import Feedback, VoteUpdate
import Services.feedback_service as feedback_service

import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error

router = APIRouter()


@router.put("/upvote")
def set_upvote(voteUpdate: VoteUpdate, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:        
        feedback_service.vote(voteUpdate.entityType, voteUpdate.entityID, current_user, 'upvote')
        # feedback_service.set_upvote(voteUpdate.entityType, voteUpdate.entityID)
        response.status_code = HTTPStatus.OK
        return {"message": f"{current_user} upvoted {voteUpdate.entityType} with ID {voteUpdate.entityID}"}
    except ValueError as err:
        err_json = create_json_for_error("Upvote error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
@router.put("/downvote")
def set_downvote(voteUpdate: VoteUpdate, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        feedback_service.vote(voteUpdate.entityType, voteUpdate.entityID, current_user, 'downvote')
        response.status_code = HTTPStatus.OK
        return {"message": f"{current_user} downvoted {voteUpdate.entityType} with ID {voteUpdate.entityID}"}
    except ValueError as err:
        err_json = create_json_for_error("Downvote error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
@router.put("/unvote")
def set_downvote(voteUpdate: VoteUpdate, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        feedback_service.unvote(voteUpdate.entityType, voteUpdate.entityID, current_user)
        response.status_code = HTTPStatus.OK
        return {"message": f"{current_user} unvoted {voteUpdate.entityType} with ID {voteUpdate.entityID}"}
    except ValueError as err:
        err_json = create_json_for_error("Unvote error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
@router.get("/check", status_code=200)
def check(entityType: str = Query(None, description="Entity type (needs, resources, actions, events)"),
            entityID: str = Query(None, description="ID of the entity"), 
            current_user: str = Depends(authentication_service.get_current_username)):
    try:
        response = feedback_service.check_feeedback(entityType, entityID, current_user)
        return {"message": response}
    except ValueError as err:
        raise HTTPException(status_code = 500, detail="An error occured while checking the user feedback")