from fastapi import Depends, FastAPI
from fastapi.middleware.cors import CORSMiddleware

from Controllers import resource_controller

app = FastAPI(debug=True)

app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(resource_controller.router)

@app.get("/")
async def root():
    return {"message": "Hello Bigger Applications, check!"}