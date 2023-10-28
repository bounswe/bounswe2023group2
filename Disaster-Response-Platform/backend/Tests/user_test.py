from fastapi.testclient import TestClient
from main import app
from Database.mongo import MongoDB
client = TestClient(app)
db = MongoDB.getInstance()



correct_signup_body= {
  "username": "ne354631",
  "password": "a2345678",
  "email": "b423um2@ou2tl21ook.com",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "phone_number": "05345783149",
  "is_email_verified": False,
  "private_account": True
}

missing_field_signup_body= {
  "username": "begum",
  "password": "a2345678",
  "email": "begum@outlook.com",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "phone_number": "05345783149",
  "is_email_verified": False,
  "private_account": False
}
wrong_phone_number_signup_body= {
  "username": "begum",
  "password": "a2345678",
  "email": "begum@outlook.com",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "phone_number": "09345783149",
  "is_email_verified": False,
  "private_account": False
}


only_email_signup_body= {
  "username": "230420832begum",
  "password": "a2345678",
  "email": "begm@ou22.com",
  "disabled": False,
  "first_name": "Begum",
  "last_name": "Arslan",
  "is_email_verified": False,
  "private_account": True
}

only_min_fields_signup_body= {
  "username": "begum",
  "password": "a2345678",
  "email": "begum@outlook.com",
  "first_name": "Begum",
  "last_name": "Arslan"
}

short_password_signup_body= {
  "username": "begum",
  "password": "a2345",
  "email": "begum@outlook.com",
  "first_name": "Begum",
  "last_name": "Arslan"
}

digitless_password_signup_body= {
  "username": "begum",
  "password": "asjkhdkshd",
  "email": "begum@outlook.com",
  "first_name": "Begum",
  "last_name": "Arslan"
}

wrong_email_signup_body= {
  "username": "begum",
  "password": "asjkhdkshd",
  "email": "begumoutlook.com",
  "first_name": "Begum",
  "last_name": "Arslan"
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