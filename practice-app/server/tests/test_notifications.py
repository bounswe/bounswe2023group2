from fastapi.testclient import TestClient
from pydantic import BaseModel
from database.baseSchema import BaseSchema
from main import app
from config import Config
from database.mongo import MongoDB
client = TestClient(app)
db = MongoDB.getInstance()
class Subscription(BaseModel):
    token: str
    topic: str

def test_read_notifications():
    response = client.get("/notifications")
    assert response.status_code == 200
    assert response.json() == {'message': 'This is notifications'}


def test_post_subscription():
    subscription= {"token": Config.LOCAL_DEVICE_TOKEN_FOR_TEST, "topic": "Food"}
    response = client.post("/notifications/subscriptions", json=subscription)
    assert response.status_code == 200
    assert response.json() == {
        "success_count": 1,
        "message": "Tokens were subscribed successfully"
    }
def test_post_unsubscription():
    subscription= {"token": Config.LOCAL_DEVICE_TOKEN_FOR_TEST, "topic": "Food"}
    response = client.post("/notifications/unsubscribe", json=subscription)
    assert response.status_code == 200
    assert response.json() == {
        "success_count": 1,
        "message": "Tokens were unsubscribed successfully"
    }
def test_get_subscriptions():
    response = client.get("/notifications/subscriptions")
    subscriptionDb = db.get_collection("subscription")
    subscriptions = [BaseSchema.dump(x) for x in list(subscriptionDb.find({}))]    
    assert response.status_code == 200
    assert response.json() == {
        "subscriptions": subscriptions,
    }
def test_get_subscriptions_of_a_client():
    response = client.get(f"/notifications/subscriptions/{Config.LOCAL_DEVICE_TOKEN_FOR_TEST}")  
    assert response.status_code == 200
    assert response.json() == list(["Clothes","Driver"])
    
def test_post_notification_to_topic():
    notification= {"topic": "Food", "title":"Test notification", "body":"test_topic"}
    response = client.post(f"/notifications/send_notification",json=notification)  
    assert response.status_code == 200
    assert response.json() == {
        "notification": "test_topic",
        "message": "Notification is sended"
    }
    
def test_post_notification_to_one():
    notification= {"topic": "Food", "title":"Test notification", "body":"test_one"}
    response = client.post(f"/notifications/send_notification",json=notification)  
    assert response.status_code == 200
    assert response.json() == {
        "notification": "test_one",
        "message": "Notification is sended"
    }

def test_get_notification_to_topic():
    notificationDb = db.get_collection("notification")
    notifications = [BaseSchema.dump(x) for x in list(notificationDb.find({}))]
    response = client.get("/notifications/send_notification")  
    assert response.status_code == 200
    assert response.json() == notifications

