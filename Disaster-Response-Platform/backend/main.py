from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from Controllers import athentication_service

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(
    athentication_service.router,
    prefix="/authenticate",
    tags=["login"],
)

@app.get("/")
async def root(request: Request):
    smarty = False
    auth_header_value = request.headers.get('Authorization', None)
    if auth_header_value:
        parts = auth_header_value.split()
        if (parts[0].lower() == 'bearer'):
            if (len(parts) == 2):
                smarty = True
    if smarty:
        return {"message": "Hey. Smart with authorization ha"}
    else:
        return {"message": "Hello Bigger Applications, check!"}