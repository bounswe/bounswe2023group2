import datetime
import json
from fastapi.testclient import TestClient
from main import app
from Models.user_model import *
from Models.event_model import Event
from Models.testing_event_models import EventApi
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


event_to_add:Event = Event(event_type = "Disaster", event_time = "2023-11-19 17:43", is_active = True,
                           center_location_x = 30.230, center_location_y = 23.230, max_distance_x = 10.230,  max_distance_y = 13.230,
                           short_description = "The building is on fire",
                           downvote = 0, upvote=0,
                           created_by_user = TEST_USER, created_time = "2023-11-18 16:30")

event_to_update:Event = Event(event_time = "2023-11-19 15:43", is_active = False,
                           max_distance_y = 13.630)

event_to_check_after_update:Event = Event(event_type = "Disaster", event_time = "2023-11-19 15:43", is_active = False,
                           center_location_x = 30.230, center_location_y = 23.230, max_distance_x = 10.230,  max_distance_y = 13.630,
                           short_description = "The building is on fire",
                           downvote = 0, upvote=0,
                           created_by_user = TEST_USER, created_time = "2023-11-18 16:30")

#    username=TEST_USER, language="Zulu", language_level="native")
# language_to_update:UserLanguage = UserLanguage(username=TEST_USER, language="Zulu", language_level="beginner")
# language_to_delete:UserLanguage = UserLanguage(username=TEST_USER, language="Zulu", language_level=None)

def clean_db_for_test():
    events_collection = MongoDB.get_collection('events')
    userDb = MongoDB.get_collection('authenticated_user')

    query_user = {"username": TEST_USER}
    query_event = {"created_by_user": TEST_USER}
    events_collection.delete_many(query_event)
    userDb.delete_one(query_user)
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

def create_event(event_info: EventApi, jsonParam):
    url = event_info.base_url + "/" #+ event_info.prefix
    response = client.post(url,
                           json = dict(jsonParam),
                           headers={"Authorization": f"Bearer {GLOBAL_TOKEN}"})

    assert response.status_code == HTTPStatus.OK

    return response.json()

def get_event(event_api_info: EventApi):
    url = event_api_info.base_url + "/"
    response = client.get(url,
                          headers={"Authorization": f"Bearer {GLOBAL_TOKEN}"})

    assert response.status_code == HTTPStatus.OK
    return response.json()

def get_event_by_id(event_api_info: EventApi, event_id:str):
    url = event_api_info.base_url + "/" + event_id
    response = client.get(url,
                          headers={"Authorization": f"Bearer {GLOBAL_TOKEN}"})

    assert response.status_code == HTTPStatus.OK
    return response.json()

def update_event(event_api_info: EventApi, event_id:str, jsonParam):
    url = event_api_info.base_url + "/" + event_id
    response = client.patch(url,
                          json=dict(jsonParam),
                          headers={"Authorization": f"Bearer {GLOBAL_TOKEN}"})

    assert response.status_code == HTTPStatus.OK
    return response.json()

def delete_event(event_api_info: EventApi, event_id:str):
    url = event_api_info.base_url + "/" + event_id
    response = client.delete(url,
                          headers={"Authorization": f"Bearer {GLOBAL_TOKEN}"})

    assert response.status_code == HTTPStatus.OK
    return response.json()

def test_create_event():
    prepare_for_test()
    command = EventApi(prefix="events")
    res = event_to_add.json()
    res_j = json.loads(res)
    create_event(command, res_j)

def test_get_events():
    test_create_event()
    command = EventApi(prefix="events")
    response_json = get_event(command)
    check_json = dict(event_to_add)
    check_json["_id"] = response_json["events"][0]["_id"]
    check_json["event_time"] = str(check_json["event_time"])
    check_json["created_time"] = str(check_json["created_time"])
    assert check_json == dict(response_json["events"][0])

def test_get_event_by_id():
    prepare_for_test()
    command = EventApi(prefix="events")
    res = event_to_add.json()
    res_j = json.loads(res)
    created_event = create_event(command, res_j)

    command = EventApi(prefix="events")
    response_json = get_event_by_id(command, created_event["events"][0]["_id"])
    check_json = dict(event_to_add)
    check_json["_id"] = response_json["events"][0]["_id"]
    check_json["event_time"] = str(check_json["event_time"])
    check_json["created_time"] = str(check_json["created_time"])
    assert check_json == dict(response_json["events"][0])

def test_update_event():
    prepare_for_test()
    command = EventApi(prefix="events")
    res = event_to_add.json()
    res_j = json.loads(res)
    created_event = create_event(command, res_j)

    res = event_to_update.json()
    res_j = json.loads(res)
    command = EventApi(prefix="events")
    response_json = update_event(command, created_event["events"][0]["_id"], res_j)
    check_json = dict(event_to_check_after_update)
    check_json["_id"] = response_json["events"][0]["_id"]
    check_json["event_time"] = str(check_json["event_time"])
    check_json["created_time"] = str(check_json["created_time"])
    assert check_json == dict(response_json["events"][0])

def test_delete_event():
    prepare_for_test()
    command = EventApi(prefix="events")
    res = event_to_add.json()
    res_j = json.loads(res)
    created_event = create_event(command, res_j)

    command = EventApi(prefix="events")
    delete_event(command, created_event["events"][0]["_id"])