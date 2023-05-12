from fastapi import APIRouter
from database.mongo import MongoDB
from database.registeredUser import *
from database.loginUser import *
router = APIRouter()

db = MongoDB.getInstance()

@router.post("/",) #because its prefix="/login" in main.py no need to add another endpoint in here.
async def login(loginUser: LoginUser): #from frontend api posts username and password to here to compare it with db
    userDb = db.get_collection("authenticated_user") # get the user collection to make find_one
    currentUser: RegisteredUser = userDb.find_one({"username":loginUser.username}) #document çünkü böyle geliyor datalar
    if (currentUser != None): #if there is a user with given username
        if ((currentUser["username"] == loginUser.username) & (currentUser["password"] == loginUser.password)): #if its password and username is correct
            return {"credentials_not_ok":False,"username":loginUser.username}
    return {"credentials_not_ok":True,"username":None}