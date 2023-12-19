import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends, Body
from starlette import status

import Models.annotations_model
from Models.annotations_model import W3CAnnotation, HypothesisModel
import Services.hypoth_caller_service as hypoth_caller_service

from Services.build_API_returns import create_json_for_error
from Models.annoserver_basic_models import Error

import hypothesis.hypothesis




# Change history:
# Created using resource as a template

router = APIRouter()

@router.post("/", responses={
    status.HTTP_200_OK: {"model": HypothesisModel},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}})
def create_annotation(anno: hypothesis.hypothesis.HypothesisAnnotation , response:Response):
    try:
        resp_result = hypoth_caller_service.test_hypothesis_connection_get(anno)
        #response.status_code = HTTPStatus.OK
        return json.loads(resp_result)
    except ValueError as err:
        err_json = create_json_for_error("Annotation create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
