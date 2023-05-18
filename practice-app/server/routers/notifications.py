import json
from fastapi import APIRouter, HTTPException, Path
import firebase_admin
from firebase_admin import initialize_app, credentials, firestore, messaging
from pydantic import BaseModel
import requests
from fastapi.middleware.cors import CORSMiddleware
from config import Config
from google.oauth2 import service_account
import google.auth.transport.requests
from database.mongo import MongoDB
from database.baseSchema import BaseSchema

router = APIRouter()
cred= credentials.Certificate(Config.FCM_CREDENTIALS)
firebase_admin.initialize_app(cred)
db = MongoDB.getInstance()

class Subscription(BaseModel):
    token: str
    topic: str
class Notification(BaseModel):
    topic: str
    title: str
    body: str
    
def _get_access_token():

  credentials = service_account.Credentials.from_service_account_file(Config.FCM_CREDENTIALS, scopes=["https://www.googleapis.com/auth/firebase.messaging", "https://www.googleapis.com/auth/cloud-platform"])
  request = google.auth.transport.requests.Request()
  credentials.refresh(request)
  print("bearer:" , credentials.token)
  return credentials.token

@router.get("/", )
async def read_users():
    return {"message": "This is notifications"}

# API Endpoints
@router.post("/subscriptions")
async def create_subscription(subscription: Subscription):
    # Save subscription to Firebase
    response = messaging.subscribe_to_topic(subscription.token, subscription.topic)
    subscriptionDb = db.get_collection("subscription")
    subscriptionDb.insert_one({"token": subscription.token, "topic": subscription.topic})
    response = {
        "success_count": response.success_count,
        "message": "Tokens were subscribed successfully"
    }
    return response

@router.post("/unsubscribe")
async def unsubscription(subscription: Subscription):
    response = messaging.unsubscribe_from_topic(subscription.token, subscription.topic)
    subscriptionDb = db.get_collection("subscription")
    subscriptionDb.delete_many({"token": subscription.token, "topic": subscription.topic})
    response = {
        "success_count": response.success_count,
        "message": "Tokens were unsubscribed successfully"
    }
    return response



@router.get("/subscriptions", include_in_schema=True)
async def get_subscriptions():
    try:
        subscriptionDb = db.get_collection("subscription")
        subscriptions = [BaseSchema.dump(x) for x in list(subscriptionDb.find({}))]    
        return {"subscriptions": subscriptions}
    except Exception:
        raise HTTPException(status_code=500, detail="Internal Server Error")

@router.get("/subscriptions/{device_token}", summary="DeviceInfo")
async def get_client_info(device_token: str):
        # Retrieve subscriptions from Firebase
    url = f"https://iid.googleapis.com/iid/info/{device_token}?details=true"
    print(url)
    payload = {}
    headers = {
    'Authorization': Config.FCM_AUTHORIZATION_KEY
    }
    response = requests.get(url, headers=headers, data=payload)
    if('rel' not in response.json()):
        return []
    
    topics = response.json()['rel']['topics']
    print(topics.keys())
    return list(topics.keys())


# Simulating event creation
@router.post("/send_notification_to_one")
async def send_notif(notification:Notification):
    print(notification)
    url = "https://fcm.googleapis.com/fcm/send"
    payload = json.dumps({
        "to": Config.MY_FCM_KEY ,
        "notification": {
        "body": notification.body,
        "OrganizationId": "2",
        "content_available": True,
        "priority": "high",
        "title": notification.title
        }
        })
    headers = {
    'Content-Type': 'application/json',
    'Authorization': Config.FCM_AUTHORIZATION_KEY
    }
    response = requests.request("POST", url, headers=headers, data=payload)
    myresponse = {
        "notification": notification.body,
        "message": "Notification is sended"
    }
    
    return myresponse


# Simulating event creation
@router.post("/send_notification")
async def create_event(notification:Notification):
    print("before")
    url = "https://fcm.googleapis.com//v1/projects/{}/messages:send".format(Config.FCM_PROJECT_ID)
    bearer_token=_get_access_token()
    print(notification.topic,notification.title, notification.body )
    payload = json.dumps({
    "message": {
        "topic": notification.topic,
        "notification": {
        "title": notification.title,
        "body": notification.body
        },
        "webpush": {
        "fcm_options": {
            "link": "https://practice-app.online/notifications"
        }
        }
    }
    })
    headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer {}'.format(bearer_token)
    }  
    response = requests.request("POST", url, headers=headers, data=payload)
   
    notificationDb = db.get_collection("notification")
    notificationDb.insert_one({"notif": response.text, "topic": notification.topic, "message:": notification.body })
    myresponse = {
        "notification": notification.body,
        "message": "Notification is sended"
    }
    if response.status_code >= 400:
        raise Exception(f"Failed to send notifications {response.status_code}.")
    return myresponse
  

@router.get("/send_notification")
async def get_notif():
    notificationDb = db.get_collection("notification")
    notifications = [BaseSchema.dump(x) for x in list(notificationDb.find({}))]
    return notifications
