from fastapi import Depends, FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import  user
from routers import filtersort

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
    filtersort.router,
    prefix="/filtersort",
    tags=["filtersort"],
)

@app.get("/")
async def root():
    return {"message": "Hello Bigger Applications, check!"}
