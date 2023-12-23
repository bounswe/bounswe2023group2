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
need_data = {
    "initialQuantity": 7,
    "urgency": 2,
    "unsuppliedQuantity": 6,
    "type": "Medication",
    "details": {
        "disease_name": "asthma",
        "medicine_name": "inhaler",
        "age": 0
            }
    }

updated_need_data = {
    "unsuppliedQuantity": 2,
    "details": {
       "medicine_name": "ddd",
    }
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

def test_login():
    global TOKEN, header
    response = client.post("/api/users/login", json=email_login_body)
    assert response.status_code == HTTPStatus.OK
    print(response.json())
    TOKEN =  response.json()["access_token"]
    header = {"Authorization": f"Bearer {TOKEN}"}


    
# def setup_test_environment():
    # """Set up test environment by creating a sample need."""
    # global TOKEN, TEST_USERNAME, TEST_EMAIL
    # # Signup and login only if the token is not already available
    # if TOKEN is None:
    #     signup()
    #     TOKEN = login()
    
    # headers = {
    #     "Authorization": f"Bearer {TOKEN}"
    # }
    
# def teardown_test_environment():
#     """Tear down the test environment by deleting the test user."""
#     if TOKEN is not None:
#         delete_user()

def test_create_need1():
    
    global TOKEN, need_id

    # if TOKEN is None:
    #     signup()
    #     TOKEN = login()
    token = TOKEN
        

    response = client.post("/api/needs",
                           json=need_data,
                           headers=header)

    assert response.status_code == HTTPStatus.OK
    need = response.json()["needs"][0]
    need_id = need["_id"]
    assert need_id is not None
    

def test_create_need2():
    
    # global TOKEN, need_data_wrong

    # if TOKEN is None:
    #     signup()
    #     TOKEN = login()
    # token = TOKEN
        

    response = client.post("/api/needs",
                           json=need_data_wrong,
                           headers=header)

    assert response.status_code == HTTPStatus.NOT_FOUND
    

def test_get_need1():
    response = client.get(f"/api/needs/{need_id}", headers=header)
    assert response.status_code == HTTPStatus.OK
    

    needs = response.json()
    assert needs["needs"][0]["_id"] == need_id


    
def test_get_need2():
    response = client.get(f"/api/needs/222", headers=header)
    assert response.status_code == HTTPStatus.NOT_FOUND

def test_get_all_needs():
    response = client.get("/api/needs/", headers=header)
    assert response.status_code == HTTPStatus.OK
    needs = response.json()
    assert isinstance(needs['needs'], list)
    
def test_update_need():

    response = client.put(f"/api/needs/{need_id}", json=updated_need_data, headers=header)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]
    updated_need = response.json()["needs"][0]
    assert updated_need["unsuppliedQuantity"] == updated_need_data["unsuppliedQuantity"]
    assert updated_need["details"]["medicine_name"] == updated_need_data["details"]["medicine_name"]


def test_set_initial_quantity():
    new_quantity = 44 
    response = client.put(f"/api/needs/{need_id}/initial_quantity", json={"quantity": new_quantity}, headers=header)
    assert response.status_code == HTTPStatus.OK
    res = response.json()
    assert str(new_quantity) in res["message"]

def test_get_initial_quantity():
    response = client.get(f"/api/needs/{need_id}/initial_quantity", headers=header)
    assert response.status_code == HTTPStatus.OK
    quantity_data = response.json()
    assert "Initial quantity" in quantity_data    

def test_set_unsupplied_quantity():
    new_quantity = 55 
    response = client.put(f"/api/needs/{need_id}/unsupplied_quantity", json={"quantity": new_quantity}, headers=header)
    assert response.status_code == HTTPStatus.OK
    res = response.json()
    assert str(new_quantity) in res["message"]

def test_get_unsupplied_quantity():
    response = client.get(f"/api/needs/{need_id}/unsupplied_quantity", headers=header)
    assert response.status_code == HTTPStatus.OK
    quantity_data = response.json()
    assert "Unsupplied quantity" in quantity_data    



def test_set_urgency():
    new_urgency = 5
    response = client.put(f"/api/needs/{need_id}/urgency", json={"urgency": new_urgency}, headers=header)
    assert response.status_code == HTTPStatus.OK
    res = response.json()
    assert str(new_urgency) in res["message"]

def test_get_urgency():
    response = client.get(f"/api/needs/{need_id}/urgency", headers=header)
    assert response.status_code == HTTPStatus.OK
    urgency_data = response.json()
    assert "Urgency" in urgency_data     





    
def test_delete_need1():
    # # Clean up: Delete the created need
    response = client.delete(f"/api/needs/{need_id}", headers=header)
    assert response.status_code == HTTPStatus.OK
    
def test_delete_need2():
    response = client.delete(f"/api/needs/222", headers=header)
    assert response.status_code == HTTPStatus.NOT_FOUND

