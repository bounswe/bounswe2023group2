from fastapi import APIRouter
from Models.resource_model import Resource
import Services.resource_service as resource_service

router = APIRouter()

@router.post("/resource/")
def create_resource(resource: Resource):
    return resource_service.create_resource(resource)

@router.get("/resource/{resource_id}")
def get_resource(resource_id: str):
    return resource_service.get_resource_by_id(resource_id)

@router.put("/resource/{resource_id}")
def update_resource(resource_id: str, resource: Resource):
    resource_service.update_resource(resource_id, resource)
    return {"status": "Resource updated successfully"}

@router.delete("/resource/{resource_id}")
def delete_resource(resource_id: str):
    resource_service.delete_resource(resource_id)
    return {"status": "Resource deleted successfully"}
