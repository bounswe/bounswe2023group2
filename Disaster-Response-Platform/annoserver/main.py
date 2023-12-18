from fastapi import FastAPI, Request, Depends, Response
from fastapi.middleware.cors import CORSMiddleware
from Services.build_API_returns import *
from Controllers import annotation_controller

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ROUTES
app.include_router(annotation_controller.router, prefix = "/api/annotations", tags=["annotations"])
@app.get("/")

async def root(some_parameter:str, response:Response):
    simple_json = create_json_for_string("System up and running", "System status")
    return json.loads(simple_json)


