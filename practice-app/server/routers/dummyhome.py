from fastapi import APIRouter
from database.mongo import MongoDB
from database.registeredUser import *
from database.loginUser import *
router = APIRouter()


@router.get("/",) #because its prefix="/login" in main.py no need to add another endpoint in here.
async def dummy_home(): #from frontend api posts username and password to here to compare it with db
    return True