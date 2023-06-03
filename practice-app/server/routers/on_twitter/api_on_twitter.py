from fastapi import APIRouter,Response,status
from routers.on_twitter.twitterFunctions import TwitterFunc
from routers.on_twitter.twitterFunctions import checkConnection

router = APIRouter()

# Sample space 1LyGBqAPMQYKN
@router.get("/alive", )
async def showAlive():
    return {"Alive": "Yes"}

@router.get("/check", )
async def showCheck():
    if checkConnection():
        return {"Check": "OK"}
    else:
        return {"Check": "Failed"}
@router.post("/publishTweets/{eventId}", )
async def processTwitterSpace(eventId, response:Response):
    twitterSpace = TwitterFunc('DRaRUser')
    json_data = twitterSpace.processEventForTweet(int(eventId), response)
    return json_data

@router.get("/getPublishedTweets/{eventId}", )
async def getPublishedTweets(eventId):
    twitterSpace = TwitterFunc('DRaRUser')
    json_data = twitterSpace.getPublishedTweets(eventId)
    return json_data

@router.get("/getEvents", )
async def getEvents():
    twitterSpace = TwitterFunc('DRaRUser')
    json_data = twitterSpace.getEvents()
    return json_data

@router.get("/deletePublishedTweets/{eventId}", )
async def deletePublished(eventId):
    twitterSpace = TwitterFunc('DRaRUser')
    json_data = twitterSpace.deletePublishedTweets(eventId)
    return json_data