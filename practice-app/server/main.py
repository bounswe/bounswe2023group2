from fastapi import Depends, FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import  user, timezone
import db
from add_activity_api.main import app as add_activity_app

# For Docker

app = FastAPI()
app.include_router(add_activity_app)

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
    timezone.router,
    prefix="/timezone",
    tags=["timezone"],
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
