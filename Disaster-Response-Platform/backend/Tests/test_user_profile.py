import datetime
import json
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

GLOBAL_TOKEN = ""
user_to_create:CreateUserRequest = CreateUserRequest(username = TEST_USER, first_name="Mahir", last_name = "Testeden",
                                                 phone_number = "05325325353", is_email_verified = False, private_account = False,
                                                 password = TEST_PASS, email = "testi@boun.edu.tr")
correct_login_request :LoginUserRequest = LoginUserRequest(username_or_email_or_phone = TEST_USER, password = TEST_PASS)
incorrect_login_request :LoginUserRequest = LoginUserRequest(username_or_email_or_phone = TEST_USER, password = "INCORRECT")

language_to_add:UserLanguage = UserLanguage(username=TEST_USER, language="Zulu", language_level="native")
language_to_update:UserLanguage = UserLanguage(username=TEST_USER, language="Zulu", language_level="beginner")
language_to_delete:UserLanguage = UserLanguage(username=TEST_USER, language="Zulu", language_level=None)


profession_to_add:UserProfession = UserProfession(username = TEST_USER, profession = "Truck Driver", profession_level= "certified pro")
skill_to_add:UserSkill = UserSkill(username=TEST_USER, skill_definition="Truck Driver", skill_level="skilled")
socialmedia_link_to_add = UserSocialMediaLink(username=TEST_USER, platform_name="X",
                                              profile_URL="https://twitter.com/testici")
user_optionalinfo_to_add = UserOptionalInfo(username=TEST_USER,
                                            date_of_birth=datetime.datetime.strptime("1990-10-12", "%Y-%m-%d"), nationality="TC",
                                           identity_number="139090909090", education="orta",
                                           ealth_condition="Hepimizi gömer", blood_type="B Rh+",
                                           Address="O cadde bu sokak Kadıköy/İstanbul")

def clean_db_for_test():
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

def prepare_for_test():
    global GLOBAL_TOKEN
    clean_db_for_test()
    create_user()
    token = do_login()
    GLOBAL_TOKEN = token

def create_user():
    response = client.post("/api/users/signup", json=dict(user_to_create))
    assert response.status_code == HTTPStatus.CREATED


def do_login():
    response = client.post("/api/users/login", json=dict(correct_login_request))

    assert response.status_code == HTTPStatus.OK

    token = response.json()["access_token"]

    assert token is not None
    return token

def create_profile_info_subitems(profile_sub_info: ProfileInfoApi, jsonParam):
    url = profile_sub_info.base_url + "/" + profile_sub_info.prefix +"/add-" + profile_sub_info.name
    response = client.post(url,
                           json = dict(jsonParam),
                           headers={"Authorization": f"Bearer {GLOBAL_TOKEN}"})

    assert response.status_code == HTTPStatus.OK

    return response.json()

def test_create_language():
    prepare_for_test()
    command = ProfileInfoApi( prefix = "languages", name = "language")
    create_profile_info_subitems(command, language_to_add)

def test_create_skills():
    prepare_for_test()
    command = ProfileInfoApi(prefix="skills", name="skill")
    create_profile_info_subitems(command, skill_to_add)

def test_create_socialmedia_links():
    prepare_for_test()
    command = ProfileInfoApi(prefix="socialmedia-links", name="socialmedia-link")
    create_profile_info_subitems(command, socialmedia_link_to_add)

def test_create_professions():
    prepare_for_test()
    command = ProfileInfoApi(prefix="professions", name="profession")
    create_profile_info_subitems(command, profession_to_add)

def test_create_optionalinfos():
    prepare_for_test()
    command = ProfileInfoApi(prefix="user-optional-infos", name="user-optional-info")
    res = user_optionalinfo_to_add.json()
    res_j = json.loads(res)
    create_profile_info_subitems(command, res_j)

def test_create_get_languages():
    test_create_language()
    command = ProfileInfoApi(prefix="languages", name="language")
    response_json = create_get_profile_info_subitems(command)
    assert dict(language_to_add) == dict(response_json["user_languages"][0])

def test_create_get_skills():
    test_create_skills()
    command = ProfileInfoApi(prefix="skills", name="skill")
    response_json = create_get_profile_info_subitems(command)
    assert dict(skill_to_add) == dict(response_json["user_skills"][0])

def test_create_get_professions():
    test_create_professions()
    command = ProfileInfoApi(prefix="professions", name="profession")
    response_json = create_get_profile_info_subitems(command)
    assert dict(profession_to_add) == dict(response_json["user_professions"][0])

def test_create_get_socialmedia_links():
    test_create_socialmedia_links()
    command = ProfileInfoApi(prefix="socialmedia-links", name="socialmedia-link")
    response_json = create_get_profile_info_subitems(command)
    assert dict(socialmedia_link_to_add) == dict(response_json["user_socialmedia_links"][0])

def test_create_get_optionalinfos():
    test_create_optionalinfos()
    command = ProfileInfoApi(prefix="user-optional-infos", name="user-optional-info")
    response_json = create_get_profile_info_subitems(command)
    res = user_optionalinfo_to_add.json()
    res_j = json.loads(res)
    if (response_json["user_optional_infos"][0]["date_of_birth"] is not None):
        response_json["user_optional_infos"][0]["date_of_birth"] = (
            datetime.datetime.strptime(response_json["user_optional_infos"][0]["date_of_birth"], "%Y-%m-%d  %H:%M:%S"))
        response_json["user_optional_infos"][0]["date_of_birth"] = response_json["user_optional_infos"][0]["date_of_birth"].strftime("%Y-%m-%d")
    assert dict(res_j) == dict(response_json["user_optional_infos"][0])

def create_get_profile_info_subitems(profile_sub_info: ProfileInfoApi):
    url = profile_sub_info.base_url + "/" + profile_sub_info.prefix
    response = client.get(url,
                          headers={"Authorization": f"Bearer {GLOBAL_TOKEN}"})

    assert response.status_code == HTTPStatus.OK

    #assert dict(language_to_add) ==  dict(response.json()["user_languages"][0])

    return response.json()
