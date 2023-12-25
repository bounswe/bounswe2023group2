from typing import Annotated
import json
from http import HTTPStatus
from fastapi import APIRouter, HTTPException, Response, Depends, Body
from starlette import status

from Models.annotations_model import Annotation as Annotation
import Services.hypoth_caller_service as hypoth_caller_service

from Services.build_API_returns import create_json_for_error
from Models.annoserver_basic_models import Error
from pydantic import AnyUrl as AnyUrl
# Change history:
# Created using resource as a template

router = APIRouter()

@router.post("/", responses={
    status.HTTP_200_OK: {"model": str},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})
async def create_annotation(response:Response, anno: Annotation = Body(
    example={
                    "id": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b",
                    "uri": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b",
                    "tags": [
                        "api", "DAPP", "rescue", "resource"
                    ],
                    "document": {
                        "title": "DAPP-Anno resourcetest 65776cc6d80e934140000000b",
                        "link": [{"href": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b"}]
                    },
                    "target": [
                        {"type": "Data", "selector": [{"type": "DataSelector",
                                                       "value": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b"}],
                         "source": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b"}
                    ],
                    "body": {"type": "Text",
                             "value": "Kaynağın verdiği bilgi ve önerisi: Sağladığı yemeklerin [PSAKD binasından](https://maps.app.goo.gl/hq6PxMbf4N8SWNTV7) yüklenebileceğini, yakın yerlerde olursa taşımayı kendisinin yapabileceğini söylüyor. [Fotoğrafını görebilirsiniz](https://zaytung.com/voicepics//Ahmad.jpg) "},
                    "motivation": {"type": "bookmarking"}
            })):
    """
    Create annotation

    **Parameters**

    - **anno**: Annotation info - check the model

    - Note that both the model and the API implementation is agnostic to the calling application.
    Although the server is coded and implemented as a part of DAPP Group 2 Project, it is simply a public annotation server.

    """
    try:

        resp_result = hypoth_caller_service.hypothesis_store(anno)
        if (resp_result.status_code == HTTPStatus.OK):
            response.status_code = HTTPStatus.OK
            return json.loads(resp_result.text)
        else:
            response.status_code = resp_result.status_code
            return json.loads(resp_result.text)
    except ValueError as err:
        err_json = create_json_for_error("Annotation create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.patch("/{id}", responses={
    status.HTTP_200_OK: {"model": str},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}
})

async def update_annotation(response:Response, id:str, anno: Annotation = Body(
    example={
                    "id": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b",
                    "uri": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b",
                    "tags": [
                        "api", "DAPP", "rescue", "resource"
                    ],
                    "document": {
                        "title": "DAPP-Anno resourcetest 65776cc6d80e934140000000b",
                        "link": [{"href": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b"}]
                    },
                    "target": [
                        {"type": "Data", "selector": [{"type": "DataSelector",
                                                       "value": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b"}],
                         "source": "https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b"}
                    ],
                    "body": {"type": "Text",
                             "value": "Kaynağın verdiği bilgi ve önerisi: Sağladığı yemeklerin [PSAKD binasından](https://maps.app.goo.gl/hq6PxMbf4N8SWNTV7) yüklenebileceğini, yakın yerlerde olursa taşımayı kendisinin yapabileceğini söylüyor. [Fotoğrafını görebilirsiniz](https://zaytung.com/voicepics//Ahmad.jpg) "},
                    "motivation": {"type": "bookmarking"}
            })):
    """
    Updates the annotation
    EXPERIMENTAL FOR NOW - PLEASE WAIT FOR NEXT UPDATE

    **Parameters**

    - **id** system id of the annotation
    - **anno**: Annotation info - check the model
    - Check the id field contains the id value returned after create

    - Note that both the model and the API implementation is agnostic to the calling application.
    Although the server is coded and implemented as a part of DAPP Group 2 Project, it is simply a public annotation server.

    """
    try:

        resp_result = hypoth_caller_service.hypothesis_update(id, anno)
        if (resp_result.status_code == HTTPStatus.OK):
            response.status_code = HTTPStatus.OK
            return json.loads(resp_result.text)
        else:
            response.status_code = resp_result.status_code
            return json.loads(resp_result.text)
    except ValueError as err:
        err_json = create_json_for_error("Annotation create error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)

@router.get("/", responses={
    status.HTTP_200_OK: {"model": str},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}})

def search_annotations(response:Response, url:AnyUrl=None, hidden=False, tag:str=None):
    """
        Retreives annotation on given url or onnotation with the given tag


        **Parameters**

        - **url**: URL for the annotated target
        - **tag**: The annotations tagged by this will be retreied. Note that tag is comma seperated list and the result set includes annotations with ALL of the tags in the list.
        - **hidden**: Retreive the hidden annotations. Default is false.

        - Note that both the model and the API implementation is agnostic to the calling application.
        Although the server is coded and implemented as a part of DAPP Group 2 Project, it is simply a public annotation server.

        """
    try:

        resp_result = hypoth_caller_service.hypothesis_fetch(url, tag, hidden)
        if (resp_result.status_code == HTTPStatus.OK):
            response.status_code = HTTPStatus.OK
            return json.loads(resp_result.text)
        else:
            response.status_code = resp_result.status_code
            return json.loads(resp_result.text)
    except ValueError as err:
        err_json = create_json_for_error("Annotation get error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
@router.delete("/{id}", responses={
    status.HTTP_200_OK: {"model": str},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_403_FORBIDDEN: {"model": Error}})

def delete_annotations(response:Response, id:str):
    """
           Deletes the annotation with the given id


           **Parameters**

           - **id**: id of the annotation to be deleted
           Note that this id is the id given by the hypothes.is system and is retreived on search
           Attention: This id is not the id used in the create body. Rather, is the one in the response of the server
           for create. It is also retrieved in search.
           eg. TU28XJ53Ee6WHDsdl_52JA
           *NOT*  https://dapp.kisa.net.tr/resources/annotations/65776cc6d80e934140000000b

           - Note that both the model and the API implementation is agnostic to the calling application.
           Although the server is coded and implemented as a part of DAPP Group 2 Project, it is simply a public annotation server.

           """
    try:

        resp_result = hypoth_caller_service.hypothesis_delete(id)
        if (resp_result.status_code == HTTPStatus.OK):
            response.status_code = HTTPStatus.OK
            return json.loads(resp_result.text)
        else:
            response.status_code = resp_result.status_code
            return json.loads(resp_result.text)
    except ValueError as err:
        err_json = create_json_for_error("Annotation delete error", str(err))
        response.status_code = HTTPStatus.NOT_FOUND
        return json.loads(err_json)
