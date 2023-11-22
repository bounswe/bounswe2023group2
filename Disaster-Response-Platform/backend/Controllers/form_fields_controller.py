import json
from fastapi import APIRouter, HTTPException
from typing import Any, Dict

def load_fields(file_path: str) -> Dict[str, Any]:
    with open(file_path, 'r') as file:
        return json.load(file)

router = APIRouter()

FORM_FIELDS_FILE_PATH = '/app/Controllers/form_fields.json'

@router.get("/resource")
async def get_resource_fields():
    form_fields = load_fields(FORM_FIELDS_FILE_PATH)
    return form_fields.get('resource', {})

@router.get("/need")
async def get_need_fields():
    form_fields = load_fields(FORM_FIELDS_FILE_PATH)
    return form_fields.get('need', {})

@router.get("/type/{type_name}")
async def get_type_details(subtype_name: str):
    form_fields = load_fields(FORM_FIELDS_FILE_PATH)
    type_details = form_fields.get('type', {})
    if type_name not in type_details:
        raise HTTPException(status_code=404, detail="Type not found")
    return type_details[type_name]
