from fastapi import FastAPI, Request, Depends, Response
from fastapi.middleware.cors import CORSMiddleware
import Services.authentication_service as authentication_service
from Services import resource_service
from Services.build_API_returns import *

from Controllers import resource_controller, user_controller, user_profile_controller

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
app.include_router(resource_controller.router, prefix = "/api/resource", tags=["resource"])
app.include_router(user_controller.router, prefix= "/api/users", tags=["users"])
app.include_router(user_profile_controller.router, prefix= "/api/profiles", tags=["user-profiles"])


@app.get("/")
async def root(some_parameter:str, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    simple_json = create_json_for_string("System up and running", "System status")
    return json.loads(simple_json)


