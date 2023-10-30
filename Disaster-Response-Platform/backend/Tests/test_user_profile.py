import json

from fastapi.testclient import TestClient
from main import app
from Models.user_model import *
from Models.user_profile_model import *
from http import HTTPStatus
client = TestClient(app)

def any_user_create():
    result:CreateUserRequest = CreateUserRequest(username = "testi", first_name="Mahir", last_name = "Testeden",
                                                 phone_number = "05325325353", is_email_verified = False, private_account = False,
                                                 password = "tesTi2023!", email = "testi@boun.edu.tr")
    return result

def user_login():
    result:LoginUserRequest = LoginUserRequest(username_or_email_or_phone = "testi", password = "tesTi2023!")
    return result

def user_language():
    result:UserLanguage = UserLanguage(username=None, language="Zulu", language_level="native")
    return result
def atest_create_user():
    # url_params = {"username": "mehmetk", "password": "CMPE451"}
    sign_user = any_user_create()
    response = client.post("/api/users/signup", json=dict(sign_user))

    print(response.json())
    assert response.status_code == HTTPStatus.CREATED

    user_id = response.json()["inserted_id"]

    assert user_id is not None
    return user_id

def test_login():
    login_user = user_login()
    response = client.post("/api/users/login", json=dict(login_user))

    print(response.json())
    assert response.status_code == HTTPStatus.OK

    token = response.json()["access_token"]

    assert token is not None
    return token

def test_create_profile():
    token = test_login()
    language = user_language()

    response = client.post("/api/profiles/languages/add-language", json=dict(language))

    print(response.json())
    assert response.status_code == HTTPStatus.OK

    token = response.json()["access_token"]

    assert token is not None
    return token
