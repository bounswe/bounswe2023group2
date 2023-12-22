import sys
sys.path.append("..")
import random
import string
from fastapi.testclient import TestClient
from main import app
from http import HTTPStatus
from datetime import datetime, timedelta
from Models.email_verification_model import EmailVerification  # Adjust the import based on your project structure

client = TestClient(app)

# Token can be set
TOKEN = None
header = None
username = None

def generate_random_string(length=10):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def create_email_verification_entry(username, email, token):
    expiration_time = datetime.now() + timedelta(hours=3) + timedelta(minutes=10)
    return EmailVerification(username=username, email=email, token=token, expiration=expiration_time)

def test_send_verification_request():
    global TOKEN, header, username
    username = generate_random_string()
    email = f"{username}@example.com"

    # authentication endpoint is "/api/email_verification/send"
    response = client.post("/api/email_verification/send", json={"username": username, "email": email})
    
    assert response.status_code == HTTPStatus.OK
    result = response.json()
    
    if "message" in result:
        assert result["message"] == "Verification email sent."
    elif "detail" in result:
        assert False, f"Failed to send verification email: {result['detail']}"
    else:
        assert False, "Unexpected response format"

def test_verify_email_success():
    global TOKEN, header, username
    token = "123456"
    verification_entry = create_email_verification_entry(username, f"{username}@example.com", token)
    response = client.post("/api/email_verification/verify", json={"token": token, "username": username})
    
    assert response.status_code == HTTPStatus.OK
    result = response.json()
    
    if "message" in result:
        assert result["message"] == "Email verified successfully."
    elif "detail" in result:
        assert False, f"Verification failed: {result['detail']}"
    else:
        assert False, "Unexpected response format"

def test_verify_email_invalid_token():
    global TOKEN, header, username
    token = "654321"
    verification_entry = create_email_verification_entry(username, f"{username}@example.com", token)
    response = client.post("/api/email_verification/verify", json={"token": "invalid_token", "username": username})
    
    assert response.status_code == HTTPStatus.BAD_REQUEST
    result = response.json()
    if "detail" in result:
        assert result["detail"] == "Invalid token or token expired"
    else:
        assert False, "Unexpected response format"


def test_delete_verification_entry():
    global username
    # Assuming your authentication endpoint is "/api/email-verification/delete"  
    response = client.post("/api/email_verification/delete", json={"username": username})
    
    assert response.status_code == HTTPStatus.OK
    result = response.json()
    
    if "message" in result:
        assert result["message"] == "Verification entry deleted."
    else:
        assert False, "Unexpected response format"

def test_check_expiration():
    global username
    # Assuming your authentication endpoint is "/api/email-verification/check-expiration"
    response = client.post("/api/email_verification/check-expiration", json={"username": username})
    
    assert response.status_code == HTTPStatus.OK
    result = response.json()
    
    if "message" in result:
        assert result["message"] == "Verification entry is not expired."
    else:
        assert False, "Unexpected response format"

def test_check_expiration_expired():
    global username
    # Expired verification entry
    token = "987654"
    verification_entry = create_email_verification_entry(username, f"{username}@example.com", token)
    verification_entry.expiration = datetime.now() - timedelta(hours=3)

    # Assuming your authentication endpoint is "/api/email-verification/check-expiration"
    response = client.post("/api/email_verification/check-expiration", json={"username": username})
    
    assert response.status_code == HTTPStatus.NOT_FOUND
    result = response.json()
    
    if "message" in result:
        assert result["message"] == "Verification entry not found or expired."
    else:
        assert False, "Unexpected response format"

def test_check_token():
    global username

    # Assuming your authentication endpoint is "/api/email-verification/check-token"
    response = client.post("/api/email-verification/check-token", json={"token": "123456", "username": username})
    
    assert response.status_code == HTTPStatus.OK
    result = response.json()
    
    if "message" in result:
        assert result["message"] == "Token is valid."
    else:
        assert False, "Unexpected response format"

def test_check_token_invalid():
    global username

    # Create a verification entry with a different token
    token = "654321"
    verification_entry = create_email_verification_entry(username, f"{username}@example.com", token)

    # Assuming your authentication endpoint is "/api/email-verification/check-token"
    response = client.post("/api/email-verification/check-token", json={"token": "invalid_token", "username": username})
    
    assert response.status_code == HTTPStatus.BAD_REQUEST
    result = response.json()
    
    if "detail" in result:
        assert result["detail"] == "Invalid token or token expired"
    else:
        assert False, "Unexpected response format"

# Run the tests
test_send_verification_request()
test_verify_email_success()
test_verify_email_invalid_token()
test_delete_verification_entry()
test_check_expiration()
test_check_expiration_expired()
test_check_token()
test_check_token_invalid()
