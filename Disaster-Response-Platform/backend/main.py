from fastapi import FastAPI, Request, Depends
from fastapi.middleware.cors import CORSMiddleware

import Services.authentication
from Controllers import authentication_controller, resource_controller
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

@app.middleware("http")
async def check_authorization(request: Request, call_next):
    if (not request.url.components.path.startswith("/api/")):
        response = await call_next(request)
        return response

    if (request.url.components.path.startswith("/api/authenticate/login")):
        response = await call_next(request)
        return response

#Temporary code
    # So that the programmers may create users while testing.
    # Will be commented out later
    if (request.url.components.path.startswith("/api/authenticate/create-user")):
        response = await call_next(request)
        return response

    valid_header = False
    auth_header_value = request.headers.get('Authorization', None)
    bearer_token=""
    if auth_header_value:
        parts = auth_header_value.split()
        if (parts[0].lower() == 'bearer'):
            if (len(parts) == 4):
                bearer_token = parts[1]
                if (parts[2].lower() == 'username'):
                    user_name = parts[3]
                    if (Services.authentication.verify_user_session(user_name, bearer_token)):
                        valid_header = True

    if (valid_header):
        response = await call_next(request)
        return response
    else:
        #TODO API calls not requesting authorization will be handled different
        content = json.loads(create_json_for_error("Unauthorized  access", "No valid authorization in the API call"))
        response = JSONResponse(status_code=HTTPStatus.UNAUTHORIZED,
                                content=content
                                )
        return response

# ROUTES
app.include_router(authentication_controller.router, prefix="/api/authenticate", tags=["login"])
app.include_router(resource_controller.router, prefix = "/resource", tags=["resource"])



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