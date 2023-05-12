from fastapi import APIRouter
from database.mongo import MongoDB
from database.registeredUser import *
from database.loginUser import *
router = APIRouter()

@router.get("/",) #because its prefix="/dummyHome" in main.py no need to add another endpoint in here.
async def dummy_home(username: str): #from frontend api gets the username as a query parameter
    return {"username":username} #return the username to frontend 

