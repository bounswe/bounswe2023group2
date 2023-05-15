from fastapi import APIRouter
from config import Config
import requests
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
from pydantic import BaseModel
from datetime import date
router = APIRouter()
db =MongoDB.getInstance()

class New(BaseModel):
    subject: str
    language: str
    title: str
    description: str
    url: str


@router.get("/", )
async def get_news(subject: str = '', language: str = 'en', sortBy: str = "publishedAt"):
  headers = {
    'Authorization': Config.NEWS_API_KEY,
    'Content-Type': 'application/json'
  }
  print(subject, language, sortBy)
  news = requests.get(f'https://newsapi.org/v2/everything?q=${subject}&from=2023-05-09&sortBy=${sortBy}', headers=headers)
  
  return {"message": "This is news", "payload": news.json()}    


@router.post("/create", status_code = 200 )
async def create_news(news:New):
  newsDb = db.get_collection("news")
  try:
    newsDb.insert_one({"subject":news.subject, 
    "language":news.language,
    "publishedAt":date.today(),
    "title":news.title,
    "url":news.url
    })
  except:
    {"message":'cannot insert the db', "success": False}

  return {"message": "Inserted to DB", "success": True }    

