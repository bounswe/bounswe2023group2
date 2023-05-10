from fastapi import Depends, FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import  user
from add_activity_api.main import app as add_activity_app

app = FastAPI()
app.include_router(add_activity_app)

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


@app.get("/")
async def root():
    return {"message": "Hello Bigger Applications, check!"}
