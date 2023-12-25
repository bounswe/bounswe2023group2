import sys
sys.path.append("..")
from fastapi.testclient import TestClient
from main import app
import random
import json
import string
from http import HTTPStatus

from Database.mongo import MongoDB
# from Services import need_service 
# from Models import need_model
client = TestClient(app)
db = MongoDB.getInstance()

# token can be set
TOKEN = None
header = None
need_id = None

signup_body= {
  "password": "a2345678",
  "disabled": False,
  "first_name": "User",
  "last_name": "Test",
  "is_email_verified": False,
  "private_account": True
}

TEST_USERNAME = None
TEST_EMAIL = None
TEST_PHONE_NO = None

email_login_body= {
    "username_or_email_or_phone": "",
    "password": "a2345678"
}
need_data = {
  "description": "biraz yiyecek lazım",
  "initialQuantity": 20,
  "urgency": 2,
  "unsuppliedQuantity": 20,
  "type": "Food",
  "details": {
      "ssdc":"dacs"
      },
  "x": 20,
  "y": 50
}

def generate_random_string(length=10):
    """Generate a random string of letters and digits."""
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def generate_random_email():
    """Generate a random email address."""
    random_username = generate_random_string()
    return f"{random_username}@example.com"

def generate_random_phone_number():
    """Generate a random phone number with the format 05XXXXXXXX."""
    # Generate a random 8-digit number
    random_digits = ''.join([str(random.randint(0, 9)) for _ in range(9)])
    # Return the number with the 05 prefix
    return "05" + random_digits

def test_signup():
    username = generate_random_string()
    signup_body['username']  = username
    
    signup_body['email']  = generate_random_email()
    signup_body['phone_number']  = generate_random_phone_number()
    email_login_body['username_or_email_or_phone']=username
    response = client.post("/api/users/signup", json=signup_body)    
    assert response.status_code == 201
    # return user_id
def test_login():
    global TOKEN, header
    response = client.post("/api/users/login", json=email_login_body)
    assert response.status_code == HTTPStatus.OK
    print(response.json())
    TOKEN =  response.json()["access_token"]
    header = {"Authorization": f"Bearer {TOKEN}"}

def test_create_need():
    
    global TOKEN, need_id
       

    response = client.post("/api/needs",
                           json=need_data,
                           headers=header)

    assert response.status_code == HTTPStatus.OK
    need = response.json()["needs"][0]
    need_id = need["_id"]
    assert need_id is not None

def test_search_need():
    # global TOKEN, need_id
    response = client.get("/api/search/needs/yiyecek", headers=header)
    assert response.status_code == HTTPStatus.OK
    res = response.json()
    assert need_data in res["message"]