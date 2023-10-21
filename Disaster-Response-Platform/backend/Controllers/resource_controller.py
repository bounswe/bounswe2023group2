from fastapi import APIRouter, HTTPException 
from Models.resource_model import Resource
import Services.resource_service as resource_service

router = APIRouter()

@router.post("/")
def create_resource(resource: Resource):
    resource_id = resource_service.create_resource(resource)
    return {"status": "Resource added successfully", "resource_id": resource_id}


@router.get("/{resource_id}")
def get_resource(resource_id: str):
    resource = resource_service.get_resource_by_id(resource_id)
    if resource:
        return resource
    raise HTTPException(status_code=404, detail="Resource not found")

@router.put("/{resource_id}")
def update_resource(resource_id: str, resource: Resource):
    resource_service.update_resource(resource_id, resource)
    return {"status": "Resource updated successfully"}

@router.delete("/{resource_id}")
def delete_resource(resource_id: str):
    resource_service.delete_resource(resource_id)
    return {"status": "Resource deleted successfully"}
