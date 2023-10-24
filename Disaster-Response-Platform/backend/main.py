from fastapi import FastAPI, Request, Depends
from fastapi.middleware.cors import CORSMiddleware

from Controllers import authentication_controller, resource_controller, user_controller
from fastapi.responses import JSONResponse
from http import HTTPStatus
from Services.build_API_returns import *

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# ROUTES
app.include_router(authentication_controller.router, prefix="/api/authenticate", tags=["login"])
app.include_router(resource_controller.router, prefix = "/api/resource", tags=["resource"])
app.include_router(user_controller.router, prefix= "/api/users", tags=["users"])


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