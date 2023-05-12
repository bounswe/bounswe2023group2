import json
from fastapi import APIRouter
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
router = APIRouter()

db =MongoDB.getInstance()
@router.get("/location",)
async def get_coord():
    userDb = db.get_collection("user")
    users = list(userDb.find({}))
    x_coord = [u.get("x_coord") for u in users]
    y_coord = [u.get("y_coord") for u in users]
    usernames = [u.get("username") for u in users]
    coord = [x_coord,y_coord,usernames]
    return coord

