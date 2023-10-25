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
    if (some_parameter == "Err"):
        response.status_code = HTTPStatus.NOT_ACCEPTABLE
        err_json = create_json_for_error("Error is here", "You want err? We got it!")
        return json.loads(err_json)
    elif(some_parameter == "Simple"):
        response.status_code = HTTPStatus.OK
        simple_json = create_json_for_string("Simple input", "INPUTTYPE")
        return json.loads(simple_json)
    else:
        try:
            resources = resource_service.get_resources("653942b132a6a932a869e411")
            return json.loads(resources)
        except ValueError as err:
            err_json = create_json_for_error("Error", str(err))
            response.status_code = HTTPStatus.NOT_FOUND
            return json.loads(err_json)



