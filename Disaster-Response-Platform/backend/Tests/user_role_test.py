from fastapi.testclient import TestClient
from main import app
from Database.mongo import MongoDB
client = TestClient(app)
db = MongoDB.getInstance()

correct_signup_body= {
  "username": "begummm",
  "password": "a2345678",
  "email": "begumarslan@outlook.de",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "phone_number": "05531420999",
  "is_email_verified": False,
  "private_account": True
}

phone_login_body= {
    "username_or_email_or_phone": "05531420999",
    "password": "a2345678"

}

correct_prof_req={
    "proficiency":"bilingual",
    "details":"English"
}

wrong_prof_req={
    "proficiency":"bilingua",
    "details":"English"
}
# def test_signup1():
#     response = client.post("/api/users/signup", json=
#     correct_signup_body)
#     assert response.status_code == 201

def test_proficiency_request1():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')
    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    response = client.put("/api/userroles/proficiency-request", json=
    [correct_prof_req], headers=headers)
    assert response.status_code == 201

def test_proficiency_request2():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')
    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }

    response = client.put("/api/userroles/proficiency-request", json=
    [wrong_prof_req], headers=headers)
    assert response.status_code == 405