import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends
from Models.report_model import Report
import Services.report_service as report_service

import Services.authentication_service as authentication_service
from Services.build_API_returns import create_json_for_error

router = APIRouter()

@router.post("/", status_code=201)
def create_report(report: Report, response:Response,current_user: str = Depends(authentication_service.get_current_username)):
    try:
        report.created_by = current_user
        report_result = report_service.create_report(report)
        response.status_code = HTTPStatus.OK
        return json.loads(report_result)
    except ValueError as err:
        err_json = create_json_for_error("Report create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/{report_id}")
def get_report(report_id: str, response:Response):
    try:
        report = report_service.get_report_by_id(report_id)
        response.status_code = HTTPStatus.OK
        return json.loads(report)
    except ValueError as err:
        err_json = create_json_for_error("Get report error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)    
    

@router.get("/")
def get_all_reports(response:Response):
    try:
        reports = report_service.get_reports()
        response.status_code = HTTPStatus.OK
        return json.loads(reports)
    except ValueError as err:
        err_json = create_json_for_error("Get reports error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)  

@router.put("/{report_id}")
def update_report(report_id: str, report: Report, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        updated_report = report_service.update_report(report_id, report)
    
        if updated_report:
            response.status_code = HTTPStatus.OK
            return {"reports": [updated_report]}
        else:
            raise ValueError(f"Report id {report_id} not updated")
    except ValueError as err:
        err_json = create_json_for_error("Report error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
    

    
@router.delete("/{report_id}")
def delete_report(report_id: str, response:Response, current_user: str = Depends(authentication_service.get_current_username)):
    try:
        res = report_service.delete_report(report_id)
        response.status_code=HTTPStatus.OK
        return json.loads(res)
        
    except ValueError as err:
        err_json = create_json_for_error("Report delete error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)    
    
