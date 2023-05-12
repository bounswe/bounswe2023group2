import sys
sys.path.insert(0, "add_activity_api")
from fastapi import FastAPI, Request, APIRouter
import requests
import db2 as db
from fastapi.templating import Jinja2Templates
from fastapi.responses import HTMLResponse
from models import Resource, Need, Event, Action
from const import *

app = APIRouter(prefix=api_prefix)

templates = Jinja2Templates(directory=root_path+"templates")

@app.get("/", response_class=HTMLResponse)
async def root(request: Request):
    return templates.TemplateResponse("index.html", {"request": request})

@app.get("/add/")
async def add(request: Request, name):
    if name == 'resource':
        return templates.TemplateResponse("add_resource.html", {"request": request})
    elif name == 'need':
        return templates.TemplateResponse("add_need.html", {"request": request})
    elif name == 'event':
        return templates.TemplateResponse("add_event.html", {"request": request})
    elif name == 'action':
        return templates.TemplateResponse("add_action.html", {"request": request})
    else:
        raise ApiError('Invalid Argument')

@app.post("/add/resource")
async def add_resource(resource: Resource):
    response = requests.get('https://www.uuidtools.com/api/generate/v4')
    if response.status_code != 200:
        raise ApiError('GET /tasks/ {}'.format(resp.status_code))
    uuid = response.json()[0]
    db.add_resource(uuid, resource.type, resource.location, resource.notes, resource.updated_at, resource.is_active, resource.upvotes, resource.downvotes, resource.creator_id, resource.creation_date, resource.condition, resource.quantity)
    return uuid

@app.post("/add/need")
async def add_need(need: Need):
    response = requests.get('https://www.uuidtools.com/api/generate/v4')
    if response.status_code != 200:
        raise ApiError('GET /tasks/ {}'.format(resp.status_code))
    uuid = response.json()[0]
    db.add_need(uuid, need.type, need.location, need.notes, need.updated_at, need.is_active, need.upvotes, need.downvotes, need.creator_id, need.creation_date, need.urgency, need.quantity)
    return uuid

@app.post("/add/event")
async def add_event(event: Event):
    response = requests.get('https://www.uuidtools.com/api/generate/v4')
    if response.status_code != 200:
        raise ApiError('GET /tasks/ {}'.format(resp.status_code))
    uuid = response.json()[0]
    db.add_event(uuid, event.type, event.location, event.notes, event.updated_at, event.is_active, event.upvotes, event.downvotes, event.creator_id, event.creation_date, event.duration)
    return uuid

@app.post("/add/action")
async def add_action(action: Action):
    response = requests.get('https://www.uuidtools.com/api/generate/v4')
    if response.status_code != 200:
        raise ApiError('GET /tasks/ {}'.format(resp.status_code))
    uuid = response.json()[0]
    db.add_action(uuid, action.notes, action.updated_at, action.is_active, action.upvotes, action.downvotes, action.creator_id, action.creation_date, action.start_location, action.end_location, action.status, action.used_resources, action.created_resources, action.fulfilled_needs, action.emerged_needs, action.related_events)
    return uuid

@app.get("/get/resource/ui")
async def get_resource(request: Request):
    return templates.TemplateResponse("get_resource.html", {"request": request})

@app.get("/get/resource")
async def get_resource(id=None):
    if id == None:
        return db.list_resource()
    return db.get_resource(id)

@app.get("/get/need/ui")
async def get_need(request: Request):
    return templates.TemplateResponse("get_need.html", {"request": request})

@app.get("/get/need")
async def get_need(id=None):
    if id == None:
        return db.list_need()
    return db.get_need(id)

@app.get("/get/event/ui")
async def get_event(request: Request):
    return templates.TemplateResponse("get_event.html", {"request": request})

@app.get("/get/event")
async def get_event(id=None):
    if id == None:
        return db.list_event()
    return db.get_event(id)

@app.get("/get/action/ui")
async def get_action(request: Request):
    return templates.TemplateResponse("get_action.html", {"request": request})

@app.get("/get/action")
async def get_action(id=None):
    if id == None:
        return db.list_action()
    return db.get_action(id)

@app.get("/delete/resource/ui")
async def delete_resource(request: Request):
    return templates.TemplateResponse("delete_resource.html", {"request": request})

@app.get("/delete/resource")
async def delete_resource(id):
    return db.delete_resource(id)

@app.get("/delete/need/ui")
async def delete_need(request: Request):
    return templates.TemplateResponse("delete_need.html", {"request": request})

@app.get("/delete/need")
async def delete_need(id):
    return db.delete_need(id)

@app.get("/delete/event/ui")
async def delete_event(request: Request):
    return templates.TemplateResponse("delete_event.html", {"request": request})

@app.get("/delete/event")
async def delete_event(id):
    return db.delete_event(id)

@app.get("/delete/action/ui")
async def delete_action(request: Request):
    return templates.TemplateResponse("delete_action.html", {"request": request})

@app.get("/delete/action")
async def delete_action(id):
    return db.delete_action(id)

