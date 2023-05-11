from fastapi import APIRouter
from .twitterSpaces import TwitterSpace

router = APIRouter()

@router.get("/waiting", )
async def waitingSpaces():
    return {"spacesId": "0000000"}

@router.get("/waiting/{spacesId}", )
async def waitingSpaceWithId(spacesId: str):
    twitterSpace = TwitterSpace(spacesId)
    return {"spacesId": twitterSpace.spacesID}

@router.post("/processSpace/{spacesId}", )
async def processTwitterSpace(spacesId):
    twitterSpace = TwitterSpace(spacesId)
    json_data = twitterSpace.processSpace()
    return json_data
