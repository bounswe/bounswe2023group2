from fastapi import APIRouter
from config import Config
import requests
router = APIRouter()


@router.get("/", )
async def get_news(subject: str = '', language: str = 'en', sortBy: str = "publishedAt"):
  headers = {
    'Authorization': Config.NEWS_API_KEY,
    'Content-Type': 'application/json'
  }
  print(subject, language, sortBy)
  news = requests.get(f'https://newsapi.org/v2/everything?q=${subject}&from=2023-05-09&sortBy=${sortBy}', headers=headers)
  
  return {"message": "This is news", "payload": news.json()}    

@router.get("/me", )
async def read_user_me():
    return {"username": "fakecurrentuser"}

