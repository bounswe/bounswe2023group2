from fastapi import FastAPI, Request, Depends, Response
from fastapi.middleware.cors import CORSMiddleware
import Services.authentication_service as authentication_service
from Services import resource_service
from Services.build_API_returns import *
from datetime import datetime
import  Scheduler.schedule as schedule
from Controllers import (resource_controller, user_controller, uprofile_optinfo_controller,
                         uprofile_languages_controller, uprofile_SocMed_controller, uprofile_professions_controller,
                         uprofile_skills_controller, need_controller, event_controller, user_roles_controller, 
                         form_fields_controller, report_controller, feedback_controller, uprofile_admin_controller,
                         action_controller, file_controller, email_verification_controller, geocode_controller,
                         action_controller_v2, file_controller,emergency_controller
                         ,forgot_password_controller,search_controller, recurrence_controller, simple_annotation_controller)


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
app.include_router(user_controller.router, prefix= "/api/users", tags=["users"])
app.include_router(email_verification_controller.router, prefix="/api/email_verification", tags=["Email Verification"])
app.include_router(forgot_password_controller.router, prefix="/api/forgot_password", tags=["Forgot Password - Reset"])
app.include_router(resource_controller.router, prefix = "/api/resources", tags=["resources"])
app.include_router(need_controller.router, prefix="/api/needs", tags=["needs"])
app.include_router(action_controller_v2.router, prefix = "/api/actions", tags=["actions"])
app.include_router(recurrence_controller.router, prefix="/api/recurrence", tags=["Recurrence - Recurring Activities"])
app.include_router(report_controller.router, prefix= "/api/reports", tags=["Reports"])
app.include_router(user_roles_controller.router, prefix= "/api/userroles", tags=["user-roles"])
app.include_router(event_controller.router, prefix= "/api/events", tags=["Events"])
app.include_router(emergency_controller.router, prefix="/api/emergencies", tags=["Emergencies"])
app.include_router(feedback_controller.router, prefix="/api/feedback", tags=["Feedback System"])
app.include_router(form_fields_controller.router, prefix="/api/form_fields", tags=["Need Resource Form Fields"])
app.include_router(uprofile_admin_controller.router, prefix="/api/admin", tags=["User Profiles - Admin"])
app.include_router(file_controller.router, prefix="/api", tags=["File Uploads"])
app.include_router(geocode_controller.router, prefix="/api/geocode", tags=["Geocode - Address Translation"])
app.include_router(search_controller.router, prefix="/api/search", tags=["Search"])
app.include_router(simple_annotation_controller.router, prefix="/api/simple_annotation", tags=["Using our annotation server"])
app.include_router(uprofile_optinfo_controller.router, prefix= "/api/profiles", tags=["User Profiles Optional Information"])
app.include_router(uprofile_languages_controller.router, prefix= "/api/profiles", tags=["User Profiles Language Skills"])
app.include_router(uprofile_SocMed_controller.router, prefix= "/api/profiles", tags=["User Profiles Social Media Links"])
app.include_router(uprofile_professions_controller.router, prefix= "/api/profiles", tags=["User Profiles Professions"])
app.include_router(uprofile_skills_controller.router, prefix= "/api/profiles", tags=["User Profiles Skills"])
schedule.start()
schedule.schedule_recurrence()

@app.get("/")
async def root(some_parameter:str, response:Response, current_user: str = Depends(authentication_service.get_current_user)):
    simple_json = create_json_for_string("System up and running", "System status")
    return json.loads(simple_json)


