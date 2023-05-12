import json
from fastapi import APIRouter
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
from pydantic import BaseModel


router = APIRouter()

db =MongoDB.getInstance()

class Item(BaseModel):
    x_coord: str
    y_coord: str


class ItemList(BaseModel):
    items: list[Item]



@router.get("/", )
async def get_coord():
    userDb = db.get_collection("user")
    users = list(userDb.find({}))
    users = [BaseSchema.dump(x) for x in list(userDb.find({}))]
    for u in users:
        u.pop('_id',None)
    # x_coord = [u.get("x_coord") for u in users]
    # y_coord = [u.get("y_coord") for u in users]
    # usernames = [u.get("username") for u in users]
    # coord = [x_coord,y_coord,usernames]
    return users


@router.post("/insert", )
async def insert_coord(coord_list:ItemList):
    
    userDb = db.get_collection("user")
    items = coord_list.items
    for dict in items:
        print(dict)
        userDb.insert_one({"x_coord":float(dict.x_coord),"y_coord":float(dict.y_coord)})
    print(items)

    return items

