import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends
from Models.need_model import Need, QuantityUpdate,UrgencyUpdate
import Services.need_service as need_service
import Services.feedback_service as feedback_service

import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error

router = APIRouter()


@router.put("/upvote")
def set_upvote(voteUpdate: VoteUpdate, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:        
        feedback_service.vote(voteUpdate.entityType, voteUpdate.entityID, current_user, 'upvote')
        feedback_service.set_upvote(voteUpdate.entityType, voteUpdate.entityID)
        response.status_code = HTTPStatus.OK
        return {"message": f"{current_user} upvoted need {need_id}"}
    except ValueError as err:
        err_json = create_json_for_error("Need upvote error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    
@router.put("/downvote")
def set_downvote(voteUpdate: VoteUpdate, response: Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        feedback_service.vote(voteUpdate.entityType, voteUpdate.entityID, current_user, 'downvote')
        feedback_service.set_downvote(voteUpdate.entityType, voteUpdate.entityID)
        response.status_code = HTTPStatus.OK
        return {"message": f"{current_user} downvoted need {need_id}"}
    except ValueError as err:
        err_json = create_json_for_error("Need downvote error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    