import json
from fastapi import APIRouter
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
router = APIRouter()

db = MongoDB.getInstance()
@router.get("/", )
async def read_users():
    userDb = db.get_collection("user")
    users = [BaseSchema.dump(x) for x in list(userDb.find({}))]
    return users


@router.get("/me", )
async def read_user_me():
    return {"username": "fakecurrentuser"}


@router.get("/{username}", )
async def read_user(username: str):
    return {"username": username}