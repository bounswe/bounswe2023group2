from fastapi.testclient import TestClient
from main import app
from Database.mongo import MongoDB
from bson.objectid import ObjectId

client = TestClient(app)
db = MongoDB.getInstance()


phone_login_body= {
    "username_or_email_or_phone": "begum3",
    "password": "a12345678"

}

recur_resource_body={
  "condition": "new",
  "initialQuantity": 5000,
  "currentQuantity": 5000,
  "type": "Food",
  "recurrence_rate": 1,
  "recurrence_deadline":"2023-11-30",
  "occur_at":"2023-11-26",
  "details":{"type": "Food"},
  "x": 5,
  "y": 10
}

resource_body={
  "condition": "new",
  "initialQuantity": 5000,
  "currentQuantity": 5000,
  "type": "Food",
  "occur_at":"2023-11-26",
  "details":{"type": "Food"},
  "x": 5,
  "y": 10
}

recur_need_body={
  "description": "süt lazım",
  "initialQuantity": 1100,
  "urgency": 5,
  "unsuppliedQuantity": 1100,
  "recurrence_rate": 1,
  "recurrence_deadline":"2025-10-23",
  "occur_at":"2025-01-05",
  "type": "Food",
  "details":{"type": "child"},
  "x": 5,
  "y": 10
}

need_body={
  "description": "süt lazım",
  "initialQuantity": 1300,
  "urgency": 5,
  "unsuppliedQuantity": 1300,
  "type": "Food",
  "details":{"type": "child"},
  "x": 5,
  "y": 10
}

action_body={
  "description":"I will be moving the food to çeliktepe and sultanselim",
  "type": "need_resource",
  "occur_at": "2023-11-25",
  "end_at":"2023-12-5",
  "related_groups": [
    {
      "related_needs": [
        "6563932859cb1bdbe004a3fd", "6563934259cb1bdbe004a3fe"
      ],
      "related_resources": [
        "656392df59cb1bdbe004a3f1", "6563931559cb1bdbe004a3f2"
      ]
    }

   
  ]
}


def test_action_create1():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')
    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    response= client.post("/api/actions", json=action_body,headers=headers)
    assert response.status_code == 201

def test_do_action1():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')
    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    id = "6563935902d88d2586312440"
    oid= ObjectId(id)
    response= client.put(f"/api/actions/do/{oid}",headers=headers)
    assert response.status_code == 200


