from fastapi import APIRouter

router = APIRouter()


@router.get("/", )
async def read_users():
    return [{"username": "Rick"}, {"username": "Morty"}]


@router.get("/me", )
async def read_user_me():
    return {"username": "fakecurrentuser"}


@router.get("/{username}", )
async def read_user(username: str):
    return {"username": username}