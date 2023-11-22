import json
from fastapi import APIRouter, HTTPException
from typing import Any, Dict

def load_fields(file_path: str) -> Dict[str, Any]:
    with open(file_path, 'r') as file:
        return json.load(file)

router = APIRouter()

FORM_FIELDS_FILE_PATH = '/app/Controllers/form_fields.json'

@router.get("/resource")
async def get_resource_subtype():
    form_fields = load_fields(FORM_FIELDS_FILE_PATH)
    return form_fields.get('resource', {})

@router.get("/need")
async def get_need_subtype():
    form_fields = load_fields(FORM_FIELDS_FILE_PATH)
    return form_fields.get('need', {})

@router.get("/subtype/{subtype_name}")
async def get_subtype_details(subtype_name: str):
    form_fields = load_fields(FORM_FIELDS_FILE_PATH)
    subtype_details = form_fields.get('subtype', {})
    if subtype_name not in subtype_details:
        raise HTTPException(status_code=404, detail="Subtype not found")
    return subtype_details[subtype_name]
