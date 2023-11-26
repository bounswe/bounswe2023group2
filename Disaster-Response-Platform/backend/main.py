from fastapi import FastAPI, Request, Depends, Response
from fastapi.middleware.cors import CORSMiddleware
import Services.authentication_service as authentication_service
from Services import resource_service
from Services.build_API_returns import *
from Controllers import (resource_controller, user_controller, uprofile_optinfo_controller,
                         uprofile_languages_controller, uprofile_SocMed_controller, uprofile_professions_controller,
                         uprofile_skills_controller, need_controller, event_controller, user_roles_controller, 
                         form_fields_controller, report_controller, feedback_controller,
                         action_controller)


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
app.include_router(resource_controller.router, prefix = "/api/resources", tags=["resources"])
app.include_router(need_controller.router, prefix="/api/needs", tags=["needs"])
app.include_router(user_controller.router, prefix= "/api/users", tags=["users"])
app.include_router(action_controller.router, prefix = "/api/actions", tags=["actions"])
app.include_router(user_roles_controller.router, prefix= "/api/userroles", tags=["user-roles"])
app.include_router(uprofile_optinfo_controller.router, prefix= "/api/profiles", tags=["User Profiles Optional Information"])
app.include_router(uprofile_languages_controller.router, prefix= "/api/profiles", tags=["User Profiles Language Skills"])
app.include_router(uprofile_SocMed_controller.router, prefix= "/api/profiles", tags=["User Profiles Social Media Links"])
app.include_router(uprofile_professions_controller.router, prefix= "/api/profiles", tags=["User Profiles Professions"])
app.include_router(uprofile_skills_controller.router, prefix= "/api/profiles", tags=["User Profiles Skills"])
app.include_router(report_controller.router, prefix= "/api/reports", tags=["Reports"])
app.include_router(event_controller.router, prefix= "/api/events", tags=["Events"])
app.include_router(form_fields_controller.router, prefix="/api/form_fields", tags=["Need Resource Form Fields"])
app.include_router(feedback_controller.router, prefix="/api/feedback", tags=["Feedback System"])


@app.get("/")
async def root(some_parameter:str, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    simple_json = create_json_for_string("System up and running", "System status")
    return json.loads(simple_json)


