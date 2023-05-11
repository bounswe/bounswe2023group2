import json
from fastapi import APIRouter, Path
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
    return [{"message": "This is notifications"}]


# API Endpoints
@router.post("/subscriptions")
async def create_subscription(subscription: Subscription):
    # Save subscription to Firebase
    response = messaging.subscribe_to_topic(subscription.token, subscription.topic)
    subscriptionDb = db.get_collection("subscription")
    subscriptionDb.insert_one({"token": subscription.token, "topic": subscription.topic})
    print(response.success_count , 'tokens were subscribed successfully')

@router.post("/unsubscribe")
async def unsubscription(subscription: Subscription):
    response = messaging.unsubscribe_from_topic(subscription.token, subscription.topic)
    subscriptionDb = db.get_collection("subscription")
    subscriptionDb.delete_many({"token": subscription.token, "topic": subscription.topic})
    print(response.success_count, 'tokens were unsubscribed successfully')


@router.get("/subscriptions")
async def get_subscriptions():
    subscriptionDb = db.get_collection("subscription")
    subscriptions = [BaseSchema.dump(x) for x in list(subscriptionDb.find({}))]
    return {"subscriptions": subscriptions}

@router.get("/topics")
async def get_topics():
    # Retrieve subscriptions from Firebase
    return {"topic": "Food"}, {"topic": "Driver"}

@router.get("/client/{device_token}", summary="DeviceInfo")
async def get_client_info(device_token: str = Path()):
        # Retrieve subscriptions from Firebase
    url = "https://iid.googleapis.com/iid/info/{device_token}?details=true"
    payload = {}
    headers = {
    'Authorization': Config.FCM_AUTHORIZATION_KEY
    }
    response = requests.get(url, headers=headers, data=payload)

    print(response.text)
    return response


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
    print(response.text)
    return response


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
            "link": "https://b645-193-140-194-16.ngrok-free.app/notifications"
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
    print(response.text)
    return response.text
  

@router.get("/send_notification")
async def send_notif():
    notificationDb = db.get_collection("notification")
    notifications = [BaseSchema.dump(x) for x in list(notificationDb.find({}))]
    return notifications
