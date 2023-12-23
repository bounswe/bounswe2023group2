from fastapi.testclient import TestClient
from main import app
from Database.mongo import MongoDB
client = TestClient(app)
db = MongoDB.getInstance()

import json

correct_signup_body= {
  "username": "begummm",
  "password": "a2345678",
  "email": "begumarslan@outlook.de",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "phone_number": "05531420999",
  "is_email_verified": False,
  "private_account": True
}

correct_update_body= {
  "email": "begumarslan2@outlook.de",
  "first_name": "Begum",
  "last_name": "Arslan",
  "phone_number": "05531420999",
  "private_account": False
}

missing_field_signup_body= {
  "username": "mistake",
  "password": "a234567we8",
  "email": "begum@outwelook.com",
  "disabled": False,
  "first_name": "Begum",
  "phone_number": "05355783349",
  "is_email_verified": False,
  "private_account": False
}
wrong_phone_number_signup_body= {
  "username": "begu45m",
  "password": "a2345678",
  "email": "begu45m@outlook.com",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "phone_number": "09345783149",
  "is_email_verified": False,
  "private_account": False
}


only_email_signup_body= {
  "username": "begum_email",
  "password": "a2345678",
  "email": "emailsignup@ou22.com",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "is_email_verified": False,
  "private_account": True
}

only_min_fields_signup_body= {
  "username": "begum4",
  "password": "a2345678",
  "email": "begum4@outlook.com",
  "first_name": "Begum",
  "last_name": "Arslan"
}

short_password_signup_body= {
  "username": "begum5",
  "password": "a2345",
  "email": "begum5@outlook.com",
  "first_name": "Begum",
  "last_name": "Arslan"
}

digitless_password_signup_body= {
  "username": "begum6",
  "password": "asjkhdkshd",
  "email": "begum6@outlook.com",
  "first_name": "Begum",
  "last_name": "Arslan"
}

wrong_email_signup_body= {
  "username": "begum7",
  "password": "asjkhdkshd",
  "email": "begumoutlook7.com",
  "first_name": "Begum",
  "last_name": "Arslan"
}

username_login_body= {
    "username": "begummm",
    "password": "a2345678"

}

merve_singup_body={
  "username": "merve",
  "first_name": "merve",
  "last_name": "gurbuz",
  "password": "12345merve",
  "phone_number": "05527934742",
  "email": "merve16gurbuz@gmail.com",
  "private_account": True
}

email_login_body= {
    "username_or_email_or_phone": "begumarslan@outlook.de",
    "password": "a2345678"

}

phone_login_body= {
    "username_or_email_or_phone": "05531420999",
    "password": "a2345678"

}

wrong_login_body= {
    "username_or_email_or_phone": "bIdontexits",
    "password": "a2345678"

}


def test_signup1():
    response = client.post("/api/users/signup", json=
    correct_signup_body)
    assert response.status_code == 201



def test_signup2():
    response = client.post("/api/users/signup", json=
    missing_field_signup_body)
    assert response.status_code == 422


def test_signup3():
    response = client.post("/api/users/signup", json=
    wrong_phone_number_signup_body)
    assert response.status_code == 400

def test_signup4():
    response = client.post("/api/users/signup", json=
    merve_singup_body)
    assert response.status_code == 201

def test_signup5():
    response = client.post("/api/users/signup", json=
    only_min_fields_signup_body)
    assert response.status_code == 201


def test_signup6():
    response = client.post("/api/users/signup", json=
   short_password_signup_body)
    assert response.status_code == 422

def test_signup7():
    response = client.post("/api/users/signup", json=
    digitless_password_signup_body)
    assert response.status_code == 401

def test_signup8():
    response = client.post("/api/users/signup", json=
    wrong_email_signup_body)
    assert response.status_code == 422





def test_login1():
    response = client.post("/api/users/login", json=
    email_login_body)
    assert response.status_code == 200
def test_login2():
    response = client.post("/api/users/login", json=
    email_login_body)
    assert response.status_code == 200
def test_login3():
    response = client.post("/api/users/login", json=
    phone_login_body)
    assert response.status_code == 200

def test_login4():
    response = client.post("/api/users/login", json=
    wrong_login_body)
    assert response.status_code == 401


def test_refresh_token1():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')

    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    response = client.post("/api/users/refresh-token", headers=headers)
    assert response.status_code == 200

def test_refresh_token2():
    token="dkfrgos"
    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    response = client.post("/api/users/refresh-token", headers=headers)
    assert response.status_code == 401
def test_get_me():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')

    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    response = client.get("/api/users/me", headers=headers)
    assert response.status_code == 200
def test_get_username():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')

    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    endpoint= "/api/users/" + "begum4"
    response = client.get(endpoint, headers=headers)
    assert response.status_code == 200

def test_get_username2():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')

    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    endpoint= "/api/users/" + "merve"
    response = client.get(endpoint, headers=headers)
    assert response.status_code == 403

def test_get_username3():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')

    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    endpoint= "/api/users/" + "merve22p3"
    response = client.get(endpoint, headers=headers)
    assert response.status_code == 404

def test_update_user():
    response = client.post("/api/users/login", json=
    phone_login_body)
    response_data = response.json()
    token = response_data.get('access_token')

    token = "Bearer " + token
    headers = {
    'Content-Type': 'application/json',
    'Authorization': token
    }
    response = client.put("/api/users/update-user", json=
    correct_update_body, headers=headers)
    assert response.status_code == 200

