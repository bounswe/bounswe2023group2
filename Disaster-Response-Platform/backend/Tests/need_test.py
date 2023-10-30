import sys
sys.path.append("..")
from fastapi.testclient import TestClient
from main import app
from http import HTTPStatus

from Database.mongo import MongoDB
# from Services import need_service 
# from Models import need_model
client = TestClient(app)
db = MongoDB.getInstance()

# token can be set
TOKEN = None
header = None

# header = {"Authorization": f"Bearer {TOKEN}"}

signup_body= {
  "username": "user1",
  "password": "a2345678",
  "email": "user@mail.com",
  "disabled": False,
  "first_name": "User",
  "last_name": "Test",
  "phone_number": "05531420995",
  "is_email_verified": False,
  "private_account": True
}
email_login_body= {
    "username_or_email_or_phone": "user@mail.com",
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
    


def test_signup():
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
    
    
def test_delete_need1():
    # # Clean up: Delete the created need
    response = client.delete(f"/api/needs/{need_id}", headers=header)
    assert response.status_code == HTTPStatus.OK
    
def test_delete_need2():
    response = client.delete(f"/api/needs/222", headers=header)
    assert response.status_code == HTTPStatus.NOT_FOUND


# def test_delete_user():
#     response = client.delete(f"/api/users/{signup_body['username']}", headers=header)
#     assert response.status_code == HTTPStatus.OK