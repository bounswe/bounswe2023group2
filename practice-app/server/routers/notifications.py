from fastapi import APIRouter
import firebase_admin
from firebase_admin import initialize_app, credentials, firestore, messaging
from pydantic import BaseModel

router = APIRouter()

firebase_admin.initialize_app()
class Topic(BaseModel):
    name: str
class Subscription(BaseModel):
    token: str
    topic: Topic
class Notification(BaseModel):
    topic: Topic
    title: str
    body: str
@router.get("/", )
async def read_users():
    return [{"message": "This is notifications"}]


# API Endpoints
@router.post("/subscriptions")
async def create_subscription(subscription:Subscription):
    # Save subscription to Firebase
    
    return subscription

@router.get("/subscriptions")
async def create_subscription():
    # Save subscription to Firebase
    
    return [{"message": "This is notifications"}]

@router.get("/subscriptions/{user_id}")
async def get_subscriptions(user_id: str):
    
    return {"subscriptions": subscriptions}

@router.get("/topics")
async def get_topics():
    # Retrieve subscriptions from Firebase
    return {"topic": "Food"}, {"topic": "Driver"}


# Simulating event creation
@router.post("/send_notification")
async def create_event(notification:Notification):
    message = messaging.Message(
        notification=messaging.Notification(
            title=notification.title,
            body=notification.body
        ),
        token=notification.token
    )
    response= messaging.send(message)
    return response
