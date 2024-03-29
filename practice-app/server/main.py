from fastapi import Depends, FastAPI
from fastapi.middleware.cors import CORSMiddleware

from routers import  user
from routers import  emailreport
from routers import user
from routers import timezone, tz_conversion

from routers import news
from routers import notifications
from routers import filtersort

from routers import location
from routers import wordAnalysis
from routers import login, registration, dummyhome
import db
from add_activity_api.main import app as add_activity_app
from routers.on_twitter import api_on_twitter



app = FastAPI()
app.include_router(add_activity_app)
@app.get("/docs", include_in_schema=False)
async def custom_swagger_ui_html(req):
    root_path = req.scope.get("root_path", "").rstrip("/")
    openapi_url = root_path+ 'api/' + app.openapi_url
    return get_swagger_ui_html(
        openapi_url=openapi_url,
        title="API",
    )

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


    emailreport.router,
    prefix="/emailreport",
    tags=["emailreport"],
)


app.include_router(
    timezone.router,
    prefix="/timezone",
    tags=["timezone"],
)

app.include_router(
    wordAnalysis.router,
    prefix="/word",
    tags=["word"],
)
app.include_router(
    tz_conversion.router,
    prefix="/tz_conversion",
    tags=["tz_conversion"],

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
    location.router,
    prefix="/location",
    tags=["location"],
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

app.include_router(
    api_on_twitter.router,
    prefix="/on-twitter",
    tags=["on-twitter"],
)

