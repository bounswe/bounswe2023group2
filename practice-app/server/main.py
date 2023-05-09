from fastapi import Depends, FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import user
from routers import notifications

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(
    user.router,
    prefix="/user",
    tags=["user"],
)

app.include_router(
    notifications.router,
    prefix="/notifications",
    tags=["notifications"],
)

@app.get("/")
async def root():
    return {"message": "Hello Bigger Applications, check!"}
