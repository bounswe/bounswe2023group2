from fastapi import APIRouter, HTTPException, Response
from Models.need_model import Need
import Services.need_service as need_service

router = APIRouter()

@router.post("/", status_code=201)
def create_need(need: Need):
    need_id = need_service.create_need(Need)
    return {"status": "OK", "data": [{"needId": need_id}]}

@router.get("/{need_id}")
def get_need(need_id: str):
    need = need_service.get_need_by_id(need_id)
    if need:
        return {"status": "OK", "data": {"needs": [need]}}
    raise HTTPException(status_code=404, detail={"status": "Error", "message": "need not found", "ErrorDetails": f"need {need_id} does not exist"})

@router.get("/")
def get_all_needs():
    needs = need_service.get_all_needs()
    return {"status": "OK", "data": {"needs": needs}}

@router.put("/{need_id}")
def update_need(need_id: str, need: Need):
    updated_need = need_service.update_need(need_id, need)
    if updated_need:
        return {"status": "OK", "data": {"needs": [updated_need]}}
    raise HTTPException(status_code=404, detail="need not found")
    
@router.delete("/{need_id}")
def delete_need(need_id: str):
    need_service.delete_need(need_id)
    return Response(status_code=204)
