from fastapi import Depends, FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import  user, registration, login, dummyhome
#import db
#from add_activity_api.main import app as add_activity_app

from routers import user
from routers import news
from routers import notifications
from routers import filtersort
<<<<<<< HEAD

import db
#from add_activity_api.main import app as add_activity_app
#from routers.on_twitter import api_on_twitter


app = FastAPI()
#app.include_router(add_activity_app)
=======

from routers import wordAnalysis

# import db
# from add_activity_api.main import app as add_activity_app
# from routers.on_twitter import api_on_twitter



app = FastAPI()
# app.include_router(add_activity_app)
>>>>>>> main

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
    notifications.router,
    prefix="/notifications",
    tags=["notifications"],
)
app.include_router(

    filtersort.router,
    prefix="/filtersort",
    tags=["filtersort"],
)
app.include_router(
    news.router,
    prefix="/news",
    tags=["news"],    
)

app.include_router(
    registration.router,
    prefix="/registration",
    tags=["registration"],
)

app.include_router(
    login.router,
    prefix="/login",
    tags=["login"],
)

app.include_router(
    dummyhome.router,
    prefix="/dummyhome",
    tags=["dummyhome"],
)


@app.get("/")
async def root():
    return {"message": "Hello Bigger Applications, check!"}

@app.get("/ping")
async def root():
    return {"message": "pong"}

@app.get("/dbtest")
async def dbtest():
    hasan = []
    cur = db.conn.cursor()
    cur.execute("SELECT username FROM user_details")
    for row in cur:
        for i in row:
            hasan.append(i)
    return{"message": hasan}
"""
app.include_router(
    api_on_twitter.router,
    prefix="/on-twitter",
    tags=["on-twitter"],
)
"""