import sys
from fastapi.testclient import TestClient
from main import app
import random
import json
import string
from http import HTTPStatus
from datetime import datetime, timedelta

from Models.email_verification_model import EmailVerification

client = TestClient(app)

# Token can be set
TOKEN = None
header = None
username = None

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
    
    signup_body['email']  = generate_random_email()
    signup_body['phone_number']  = generate_random_phone_number()
    email_login_body['username_or_email_or_phone']=username
    response = client.post("/api/users/signup", json=signup_body)    
    assert response.status_code == 201
    # return user_id

def generate_random_string(length=10):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def current_time_gmt3():
    return datetime.now() + timedelta(hours=3)

def create_email_verification_entry(username, email, token):
    expiration_time = current_time_gmt3() + timedelta(hours=3) + timedelta(minutes=10)
    return EmailVerification(username=username, email=email, token=token, expiration=expiration_time)

def assert_response_status(response, expected_status_code, msg=None):
    assert response.status_code == expected_status_code, (
        f"Received unexpected status code: {response.status_code}. "
        f"Expected: {expected_status_code}. {msg}\nResponse content: {response.text}"
    )

def test_send_verification_request():
    global TOKEN, header, username
    username = generate_random_string()
    email = f"{username}@example.com"

    response = client.post("/api/email_verification/send", json={"username": username, "email": email})
    
    assert_response_status(response, HTTPStatus.OK, "Send verification request failed.")

def test_verify_email_success():
    global TOKEN, header, username
    token = "123456"
    verification_entry = create_email_verification_entry(username, f"{username}@example.com", token)

    response = client.post("/api/email_verification/verify", json={"token": token, "username": username})

    assert_response_status(response, HTTPStatus.OK, "Verify email success failed.")
    result = response.json()

    assert "message" in result and result["message"] == "Email verified successfully."
    assert "email" in result and result["email"] == f"{username}@example.com"

def test_verify_email_invalid_token():
    global TOKEN, header, username
    token = "654321"
    verification_entry = create_email_verification_entry(username, f"{username}@example.com", token)

    response = client.post("/api/email_verification/verify", json={"token": "invalid_token", "username": username})

    assert_response_status(response, HTTPStatus.BAD_REQUEST, "Verify email with invalid token failed.")
    result = response.json()

    assert "detail" in result and result["detail"] == "Invalid token or token expired"
