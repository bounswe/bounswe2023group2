import json
from http import HTTPStatus
from fastapi import APIRouter, HTTPException, Response, Depends, Query
from Models.emergency_model import Emergency
import Services.emergency_service as emergency_service
# import Services.feedback_service as feedback_service
import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error
from typing import List, Optional

router = APIRouter()

@router.post("/", status_code=201)
def create_emergency(emergency: Emergency, response:Response):
    try:
        try:
            emergency.created_by_user = authentication_service.get_current_user().username
        except:
            emergency.created_by_user = "GUEST"
        
        # need.created_by = current_user
        emergency_result = emergency_service.create_emergency(emergency)
        response.status_code = HTTPStatus.OK
        return json.loads(emergency_result)
    except ValueError as err:
        err_json = create_json_for_error("Emergency create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

# @router.get("/{need_id}")
# def get_need(need_id: str, response:Response):
#     try:
#         need = need_service.get_need_by_id(need_id)
#         response.status_code = HTTPStatus.OK
#         return json.loads(need)
#     except ValueError as err:
#         err_json = create_json_for_error("Need error", str(err))
#         response.status_code = HTTPStatus.NOT_FOUND
#         return json.loads(err_json)    
    

# @router.get("/")
# def get_all_needs(
#     response: Response,
#     active: Optional[bool] = Query(None, description="Filter by active status"),
#     types: List[str] = Query(None, description="Filter by types of needs"),
#     subtypes: List[str] = Query(None, description="Filter by subtypes of needs"),
#     sort_by: str = Query('created_at', description="Field to sort by"),
#     order: Optional[str] = Query('asc', description="Sort order")
# ):

#     if types:
#         types_list = types[0].split(',')
#     else:
#         types_list = []

#     if subtypes:
#         subtypes_list = subtypes[0].split(',')
#     else:
#         subtypes_list = []

#     try:
#         needs = need_service.get_needs(
#             active=active,
#             types=types_list,
#             subtypes=subtypes_list,
#             sort_by=sort_by,
#             order=order
#         )
#         response.status_code = HTTPStatus.OK
#         return json.loads(needs)
#     except ValueError as err:
#         err_json = create_json_for_error("Need error", str(err))
#         response.status_id = HTTPStatus.NOT_FOUND
#         return json.loads(err_json)

# @router.put("/{need_id}")
# def update_need(need_id: str, need: Need, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    
#     try:
#         updated_need = need_service.update_need(need_id, need)
    
#         if updated_need:
#             response.status_code = HTTPStatus.OK
#             return {"needs": [updated_need]}
#         else:
#             raise ValueError(f"Need id {need_id} not updated")
#     except ValueError as err:
#         err_json = create_json_for_error("Need error", str(err))
#         response.status_code = HTTPStatus.NOT_FOUND
#         return json.loads(err_json)
    

    
# @router.delete("/{need_id}")
# def delete_need(need_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
#     try:
#         res = need_service.delete_need(need_id)
#         response.status_code=HTTPStatus.OK
#         return json.loads(res)
        
#     except ValueError as err:
#         err_json = create_json_for_error("Need delete error", str(err))
#         response.status_code = HTTPStatus.NOT_FOUND
#         return json.loads(err_json)    
    