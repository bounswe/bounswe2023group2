from fastapi import APIRouter
from .twitterFunctions import TwitterFunc

router = APIRouter()


# Sample space 1LyGBqAPMQYKN
@router.get("/alive", )
async def waitingSpaces():
    return {"Alive": "Yes"}

@router.get("/waiting/{spacesId}", )
async def waitingSpaceWithId(spacesId: str):
    twitterSpace = TwitterFunc(spacesId)
    return {"spacesId": twitterSpace.systemUser}

@router.get("/process/{user_id}", )
async def getTwitterSpace(user_id):
    twitterSpace = TwitterFunc(user_id)
    json_data = twitterSpace.processEventForTweet()
    return json_data

@router.post("/processEvent/{eventId}", )
async def processTwitterSpace(eventId):
    twitterSpace = TwitterFunc('DRaRUser')
    json_data = twitterSpace.processEventForTweet(eventId)
    return json_data
