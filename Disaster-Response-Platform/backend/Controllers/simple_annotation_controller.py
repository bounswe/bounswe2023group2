import json
import requests
from fastapi import APIRouter, HTTPException, Response, Depends
from starlette import status
from pydantic import  AnyUrl
from Models.annotation_server_api_model import SimpleAnnotationCreated, create_annotation_simple, Annotation, Body, Target, Motivation, Selector
from Models.user_model import Error
from Services.build_API_returns import create_json_for_error
# Change history:
# Created using resource as a template

router = APIRouter()

@router.post("/", responses={
    status.HTTP_200_OK: {"model": SimpleAnnotationCreated},
    status.HTTP_400_BAD_REQUEST: {"model": Error},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}})
def create_simple_annotation(url: str, annotation_text: str, tags:str,  response:Response, refUrl: AnyUrl=None, imageUrl: AnyUrl = None):
    """
    Create a simple annotation in our independent annotation server

    **Parameters**

    - **url**: The URL of the annotated page - sends to the resource, need etc entry page - Required
    - **tags**: Comma delimited tags - Required
    - **refUrl**: A reference URL if the user adds - Optional
    - **imageUrl**: A link to an image - Optional

    - Note that both the model and the API implementation is agnostic to the calling application.
    Although the server is coded and implemented as a part of DAPP Group 2 Project, it is simply a public annotation server.

    """
    annoBody:str = create_annotation_simple(annotation_text, refUrl, imageUrl, tags)

    annotation_data = Annotation(id=url,
                                 body=Body(type="Text", value=annoBody),
                                 target= [Target(type="Data", source= url, selector=[Selector(type="DataSelector", value=url)])],
                                 tags=tags.split(","),
                                 motivation=Motivation(type="bookmarking"),
                                 uri=url)

    hypoAPIurl = "http://annoserver:18000/api/annotations"
    data = json.dumps(annotation_data.dict())
    responseFrom = requests.request("POST", hypoAPIurl, data=data)
    if responseFrom.status_code != 200:
        err_json = create_json_for_error("Annotation create error", "Error from annoserver")
        return json.loads(err_json)

    return json.loads(responseFrom.text)

