from fastapi import Depends, FastAPI

from routers import  user

app = FastAPI()


app.include_router(
    user.router,
    prefix="/user",
    tags=["user"],
)


@app.get("/")
async def root():
    return {"message": "Hello Bigger Applications!"}
