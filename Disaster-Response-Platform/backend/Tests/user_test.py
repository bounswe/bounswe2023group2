from fastapi.testclient import TestClient
from main import app
from Database.mongo import MongoDB
client = TestClient(app)
db = MongoDB.getInstance()



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

missing_field_signup_body= {
  "username": "mistake",
  "password": "a234567we8",
  "email": "begum@outwelook.com",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "phone_number": "05355783149",
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
    "username": "begum",
    "password": "a2345678"

}

email_login_body= {
    "username": "begum",
    "password": "a2345678"

}

phone_login_body= {
    "username": "begum",
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
    assert response.status_code == 422

def test_signup4():
    response = client.post("/api/users/signup", json=
    only_email_signup_body)
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

def test_signup7():
    response = client.post("/api/users/signup", json=
    wrong_email_signup_body)
    assert response.status_code == 422





# def test_login1():
#     response = client.post("/api/users/login", json=
#     correct_signup_body)
#     assert response.status_code == 201
# def test_login2():
#     response = client.post("/api/users/login", json=
#     correct_signup_body)
#     assert response.status_code == 201