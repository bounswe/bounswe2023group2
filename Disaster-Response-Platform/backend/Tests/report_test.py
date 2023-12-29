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
report_id = None
# need_id = None

# header = {"Authorization": f"Bearer {TOKEN}"}

signup_body= {
  "password": "a2345678",
  "disabled": False,
  "first_name": "User",
  "last_name": "Test",
  "is_email_verified": False,
  "private_account": True
}



email_login_body= {
    "username_or_email_or_phone": "",
    "password": "a2345678"
}
username_data = {}
report_data = {
  "description": "This is fake need",
  "report_type": "needs"
}
updated_report_data = {
    "description": "new description"
}


report_data_wrong = {
        "description": "string",
        "report_type": "needs"
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
    username_data['username'] = username
    
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
    TOKEN =  response.json()["access_token"]
    header = {"Authorization": f"Bearer {TOKEN}"}

need_data = {
  "description": "string",
  "initialQuantity": 8,
  "quantityUnit": "piece",
  "urgency": 2,
  "unsuppliedQuantity": 8,
  "type": "string",
  "details": {
        "disease_name": "asthma",
        "medicine_name": "inhaler",
        "age": 20
            },
  "open_address": "Celiktepe",
  "x": 20,
  "y": 50,
  "occur_at": "2023-12-29"
}
def test_create_need1():
    
    global TOKEN

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
    
    report_data["report_type_id"] = need_id
    

    
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

def test_create_report1():
    
    global TOKEN, report_id

    # if TOKEN is None:
    #     signup()
    #     TOKEN = login()
    token = TOKEN
        

    response = client.post("/api/reports",
                           json=report_data,
                           headers=header)

    assert response.status_code == HTTPStatus.OK
    report = response.json()["reports"][0]
    report_id = report["_id"]
    assert report_id is not None
    

def test_create_report2():
    
    # global TOKEN, need_data_wrong

    # if TOKEN is None:
    #     signup()
    #     TOKEN = login()
    # token = TOKEN
        

    response = client.post("/api/reports",
                           json=report_data_wrong,
                           headers=header)

    assert response.status_code == HTTPStatus.NOT_FOUND
    # /api/userroles
    
def test_make_admin():
    global TOKEN
    response = client.post(f"/api/userroles/make-admin/?username={username_data['username']}",
                           headers=header)
    assert response.status_code == HTTPStatus.CREATED
    # assert response.json()["message"] == "User role updated"

def test_get_report1():
    response = client.get(f"/api/reports/{report_id}", headers=header)
    assert response.status_code == HTTPStatus.OK
    reports = response.json()
    assert reports["reports"][0]["_id"] == report_id


    
def test_get_report2():
    response = client.get(f"/api/reports/222", headers=header)
    assert response.status_code == HTTPStatus.NOT_FOUND

def test_get_all_reports():
    response = client.get("/api/reports/", headers=header)
    assert response.status_code == HTTPStatus.OK
    reports = response.json()
    assert isinstance(reports['reports'], list)
    
def test_update_report():

    response = client.put(f"/api/reports/{report_id}", json=updated_report_data, headers=header)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]
    updated_report = response.json()["reports"][0]
    assert updated_report["description"] == updated_report_data["description"]

    
def test_delete_report1():
    # # Clean up: Delete the created need
    response = client.delete(f"/api/reports/{report_id}", headers=header)
    assert response.status_code == HTTPStatus.OK
    
def test_delete_report2():
    response = client.delete(f"/api/reports/222", headers=header)
    assert response.status_code == HTTPStatus.NOT_FOUND

