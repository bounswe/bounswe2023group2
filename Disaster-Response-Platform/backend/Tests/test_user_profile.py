import json
from urllib.parse import urlencode

import httpx
from fastapi.testclient import TestClient
from main import app
from Models.user_model import *
from Models.testing_profile_models import ProfileInfoApi
from Models.user_profile_model import *
from http import HTTPStatus
from Database.mongo import MongoDB
client = TestClient(app)

TEST_USER = "testi"
TEST_PASS = "tesTi2023!"

user_to_create:CreateUserRequest = CreateUserRequest(username = TEST_USER, first_name="Mahir", last_name = "Testeden",
                                                 phone_number = "05325325353", is_email_verified = False, private_account = False,
                                                 password = TEST_PASS, email = "testi@boun.edu.tr")
correct_login_request :LoginUserRequest = LoginUserRequest(username_or_email_or_phone = TEST_USER, password = TEST_PASS)
incorrect_login_request :LoginUserRequest = LoginUserRequest(username_or_email_or_phone = TEST_USER, password = "INCORRECT")

language_to_add:UserLanguage = UserLanguage(username=TEST_USER, language="Zulu", language_level="native")
language_to_update:UserLanguage = UserLanguage(username=TEST_USER, language="Zulu", language_level="beginner")
language_to_delete:UserLanguage = UserLanguage(username=TEST_USER, language="Zulu", language_level=None)

profession_to_add:UserProfession = UserProfession(username = TEST_USER, profession = "Truck Driver", profession_level= "certified pro")

def prepare_for_test():
    profile_languages = MongoDB.get_collection('user_languages')
    profile_optional_infos = MongoDB.get_collection('user_optional_infos')
    profile_professions = MongoDB.get_collection('user_professions')
    profile_skills = MongoDB.get_collection('user_skills')
    profile_socialmedias = MongoDB.get_collection('user_socialmedias')
    userDb = MongoDB.get_collection('authenticated_user')

    query = {"username": TEST_USER}

    profile_languages.delete_one(query)
    profile_optional_infos.delete_one(query)
    profile_professions.delete_one(query)
    profile_skills.delete_one(query)
    profile_socialmedias.delete_one(query)
    userDb.delete_one(query)
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

def create_profile_info(profile_sub_info: ProfileInfoApi, jsonParam):
    prepare_for_test()
    create_user()
    token = do_login()

    url = profile_sub_info.base_url + "/" + profile_sub_info.prefix +"/add-" + profile_sub_info.name
    response = client.post(url,
                           json = dict(jsonParam),
                           headers={"Authorization": f"Bearer {token}"})

    assert response.status_code == HTTPStatus.OK

    return response.json()

def test_create_language():
    command = ProfileInfoApi( prefix = "languages", name = "language")
    create_profile_info(command, language_to_add)

def test_create_profession():
    command = ProfileInfoApi( prefix = "professions", name = "profession")
    create_profile_info(command, profession_to_add)

def test_create_get_language():
    test_create_language()
    token = do_login()

    response = client.get("/api/profiles/languages",
                           headers={"Authorization": f"Bearer {token}"})

    assert response.status_code == HTTPStatus.OK

    assert dict(language_to_add) ==  dict(response.json()["user_languages"][0])

    return response.json()

def test_update_language():
    test_create_language()
    token = do_login()

    response = client.post("/api/profiles/languages/add-language",
                           json=dict(language_to_update),
                           headers={"Authorization": f"Bearer {token}"})

    assert response.status_code == HTTPStatus.OK

    assert dict(language_to_update) == dict(response.json())

    return response.json()


def test_delete_language():
    test_create_language()
    token = do_login()

    response = client.request(method="DELETE", url="/api/profiles/languages/delete-language",
                             json= {"language":language_to_delete.language},
                           headers = {"Authorization": f"Bearer {token}"})

    assert response.status_code == HTTPStatus.OK

    #check the deleted language
    response = client.get("/api/profiles/languages", params={"language": language_to_delete.language},
                           headers={"Authorization": f"Bearer {token}"})

    assert response.status_code == HTTPStatus.OK

    assert len(response.json()["user_languages"]) == 0

    return response.json()
