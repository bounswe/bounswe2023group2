from fastapi.testclient import TestClient
from main import app
from Models.user_model import *
from Models.user_profile_model import *
from http import HTTPStatus
from Database.mongo import MongoDB
client = TestClient(app)

user_to_create:CreateUserRequest = CreateUserRequest(username = "testi", first_name="Mahir", last_name = "Testeden",
                                                 phone_number = "05325325353", is_email_verified = False, private_account = False,
                                                 password = "tesTi2023!", email = "testi@boun.edu.tr")
correct_login_request :LoginUserRequest = LoginUserRequest(username_or_email_or_phone = "testi", password = "tesTi2023!")
incorrect_login_request :LoginUserRequest = LoginUserRequest(username_or_email_or_phone = "testi", password = "INCORRECT")
language_to_add:UserLanguage = UserLanguage(username=None, language="Zulu", language_level="native")

def prepare_for_test():
    profile_languages = MongoDB.get_collection('user_languages')
    profile_optional_infos = MongoDB.get_collection('user_optional_infos')
    profile_professions = MongoDB.get_collection('user_professions')
    profile_skills = MongoDB.get_collection('user_skills')
    profile_socialmedias = MongoDB.get_collection('user_socialmedias')
    return

def create_user():
    response = client.post("/api/users/signup", json=dict(user_to_create))
    return


def do_login():
    response = client.post("/api/users/login", json=dict(correct_login_request))

    assert response.status_code == HTTPStatus.OK

    token = response.json()["access_token"]

    assert token is not None
    return token


def test_create_profile():
    create_user()
    token = do_login()

    response = client.post("/api/profiles/languages/add-language",
                           json=dict(language_to_add),
                           headers={"Authorization": f"Bearer {token}"})

    assert response.status_code == HTTPStatus.OK

    return response.json()
