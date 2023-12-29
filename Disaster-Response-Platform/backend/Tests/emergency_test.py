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
emergency_id = None

# header = {"Authorization": f"Bearer {TOKEN}"}

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
emergency_data = {
  "emergency_type": "News",
  "description": "string",
  "x": 50,
  "y": 60,
  "location": "string"
}
emergency_data_anon ={
  "contact_name": "jane doe",
  "contact_number": "0555667788",
  "emergency_type": "News",
  "description": "string",
  "x": 50,
  "y": 60,
  "location": "string"
}

updated_emergency_data = {
  "description": "new description",
  "x": 80,
  "y": 60
}


need_data_wrong = {
    "initialQuantity": 7,
    "unsuppliedQuantity": 6,
    "type": "Medication",
    "details": {
        "disease_name": "asthma",
        "medicine_name": "inhaler",
        "age": 0
            }
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

def test_create_emergency1():
    
    # global TOKEN, need_id

    # if TOKEN is None:
    #     signup()
    #     TOKEN = login()
    # token = TOKEN
        

    response = client.post("/api/emergencies/anonymous",
                           json=emergency_data_anon,
                           headers=header)

    assert response.status_code == HTTPStatus.OK
    # need = response.json()["needs"][0]
    # need_id = need["_id"]
    # assert need_id is not None
    

def test_login():
    global TOKEN, header
    response = client.post("/api/users/login", json=email_login_body)
    assert response.status_code == HTTPStatus.OK
    print(response.json())
    TOKEN =  response.json()["access_token"]
    header = {"Authorization": f"Bearer {TOKEN}"}



def test_create_emergency2():
    
    global TOKEN, emergency_id

    # if TOKEN is None:
    #     signup()
    #     TOKEN = login()
    token = TOKEN
        

    response = client.post("/api/emergencies",
                           json=emergency_data_anon,
                           headers=header)

    assert response.status_code == HTTPStatus.OK
    emergency = response.json()["emergencies"][0]
    emergency_id = emergency["_id"]
    assert emergency_id is not None
    
def test_get_emergency1():
    response = client.get(f"/api/emergencies/{emergency_id}", headers=header)
    assert response.status_code == HTTPStatus.OK
    

    emergencies = response.json()
    assert emergencies["emergencies"][0]["_id"] == emergency_id


    
def test_get_emergency2():
    response = client.get(f"/api/emergencies/600dd9feb53a0ba91e2bbcf9", headers=header)
    assert response.status_code == HTTPStatus.OK
    emergencies = response.json()
    assert isinstance(emergencies['emergencies'], list)
    assert len(emergencies['emergencies']) == 0
    

def test_get_all_emergencies():
    response = client.get("/api/emergencies/", headers=header)
    assert response.status_code == HTTPStatus.OK
    emergencies = response.json()
    assert isinstance(emergencies['emergencies'], list)
    
def test_update_emergency():

    response = client.patch(f"/api/emergencies/{emergency_id}", json=updated_emergency_data, headers=header)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]
    updated_emergency = response.json()["emergencies"][0]
    assert updated_emergency["description"] == updated_emergency_data["description"]
    assert updated_emergency["x"] == updated_emergency_data["x"]


    
def test_delete_emergency1():
    # # Clean up: Delete the created emergency
    response = client.delete(f"/api/emergencies/{emergency_id}", headers=header)
    assert response.status_code == HTTPStatus.OK
    
def test_delete_emergency2():
    response = client.delete(f"/api/emergencies/600dd9feb53a0ba91e2bbcf9", headers=header)
    assert response.status_code == HTTPStatus.NOT_FOUND

