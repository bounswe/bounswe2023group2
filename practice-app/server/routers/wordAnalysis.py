import requests
import json
from fastapi import FastAPI, Request, APIRouter
router = APIRouter()
from fastapi.templating import Jinja2Templates
from fastapi.responses import HTMLResponse
from pydantic import BaseModel
from typing import Optional
API_URL = "https://api-inference.huggingface.co/models/deprem-ml/deprem-ner"
headers = {"Authorization": "Bearer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}
templates = Jinja2Templates(directory='routers/'+"templates")

class Need(BaseModel):
    notes: str

def query(payload):
    response = requests.post(API_URL, headers={}, json=payload)
    return response.json()

@router.post("/hasan")
async def read_users(need: Need):
    output = query({
        "inputs": str(need),
    })
    print(str(need))
    jsonString =json.dumps(output)
    if "error" in jsonString:
        return output["error"]

    finalString = ""
    print(json.dumps(output))
    for data in output:
        finalString += data["entity_group"] + ": " + data["word"] + "\n"
    return finalString
@router.get("/word")
async def get_resource(request: Request):
    return templates.TemplateResponse("word_analysis.html", {"request": request})
