from fastapi import APIRouter, HTTPException, Response
from Models.resource_model import Resource
import Services.resource_service as resource_service
from bson.objectid import ObjectId

router = APIRouter()

@router.post("/", status_code=201)
def create_resource(resource: Resource):
    resource_id = resource_service.create_resource(resource)
    return {"status": "OK", "message": "Resource created", "data": [{"resourceId": resource_id}]}

@router.get("/{resource_id}")
def get_resource(resource_id: str):
    resource = resource_service.get_resource_by_id(resource_id)
    if resource:
        return {"status": "OK", "data": {"resources": [resource]}}
    raise HTTPException(status_code=404, detail={"status": "Error", "ErrorMessage": "Resource not found", "ErrorDetails": f"Resource {resource_id} does not exist"})

@router.get("/")
def get_all_resources():
    resources = resource_service.get_all_resources()
    return {"status": "OK", "data": {"resources": resources}}

@router.put("/{resource_id}")
def update_resource(resource_id: str, resource: Resource):
    updated_resource = resource_service.update_resource(resource_id, resource)
    return {"status": "OK", "data": {"resources": [updated_resource]}}

@router.delete("/{resource_id}")
def delete_resource(resource_id: str):
    resource_service.delete_resource(resource_id)
    return Response(status_code=204)
