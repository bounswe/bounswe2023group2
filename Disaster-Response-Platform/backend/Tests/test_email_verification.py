import datetime
from unittest import mock
import pytest
from fastapi import HTTPException
from Services.email_verification_service import (
    create_token,
    create_verification_entry,
    delete_verification_entry,
    send_verification_email,
    check_expiration,
    check_token,
    verify_user,
    check_verified,
)
from Models.email_verification_model import EmailVerification

# Mock MongoDB collection and current_time_gmt3 function
@pytest.fixture
def mock_email_verification_collection():
    with mock.patch("Services.email_verification_service.email_verification_collection") as mock_collection:
        yield mock_collection

@pytest.fixture
def mock_user_collection():
    with mock.patch("Services.email_verification_service.user_collection") as mock_collection:
        yield mock_collection

@pytest.fixture
def mock_current_time_gmt3():
    with mock.patch("Services.email_verification_service.current_time_gmt3") as mock_time:
        yield mock_time

def test_create_token():
    token = create_token()
    assert isinstance(token, str)
    assert len(token) == 6
    assert token.isdigit()

def test_create_verification_entry(mock_email_verification_collection):
    mock_collection = mock_email_verification_collection
    username = "test_user"
    email = "test@example.com"
    token = "123456"
    
    create_verification_entry(username, email, token)

    assert mock_collection.insert_one.called

def test_delete_verification_entry(mock_email_verification_collection):
    mock_collection = mock_email_verification_collection
    username = "test_user"
    delete_verification_entry(username)
    mock_collection.delete_one.assert_called_with({"username": username})

def test_send_verification_email(mock_email_verification_collection, mock_current_time_gmt3):
    mock_collection = mock_email_verification_collection
    mock_current_time_gmt3.return_value = datetime.datetime(2023, 1, 1, 0, 0, 0)
    username = "test_user"
    email = "test@example.com"

    success = send_verification_email(username, email)

    assert mock_collection.insert_one.called

def test_check_expiration(mock_email_verification_collection, mock_current_time_gmt3):
    mock_collection = mock_email_verification_collection
    username = "test_user"
    expiration = datetime.datetime(2023, 1, 1, 0, 10, 0)
    mock_collection.find_one.return_value = {"username": username, "expiration": expiration}

    # Test when not expired
    mock_current_time_gmt3.return_value = datetime.datetime(2023, 1, 1, 0, 5, 0)
    result = check_expiration(username)
    assert result is True

    # Test when expired
    mock_current_time_gmt3.return_value = datetime.datetime(2023, 1, 1, 0, 11, 0)
    result = check_expiration(username)
    assert result is False

def test_check_token(mock_email_verification_collection):
    mock_collection = mock_email_verification_collection
    username = "test_user"
    provided_token = "123456"
    expiration = datetime.datetime(2023, 1, 1, 0, 10, 0)
    mock_collection.find_one.return_value = {"username": username, "token": provided_token, "expiration": expiration}

    # Test invalid token
    result = check_token("new", provided_token)
    assert result is False

def test_verify_user(mock_email_verification_collection, mock_user_collection):
    mock_email_collection = mock_email_verification_collection
    mock_user_collection = mock_user_collection
    username = "test_user"
    provided_token = "123456"
    expiration = datetime.datetime(2023, 1, 1, 0, 10, 0)
    mock_email_collection.find_one.return_value = {"username": username, "token": provided_token, "expiration": expiration}
    mock_user_collection.find_one.return_value = {"username": username, "user_role": "GUEST", "is_email_verified": False}

    # Test invalid token
    result = verify_user(username, "654321")
    assert result is False

def test_check_verified(mock_user_collection):
    mock_user_collection = mock_user_collection
    username = "test_user"

    # Test verified user
    mock_user_collection.find_one.return_value = {"username": username, "is_email_verified": True}
    result = check_verified(username)
    assert result == "verified"

    # Test not verified user
    mock_user_collection.find_one.return_value = {"username": username, "is_email_verified": False}
    result = check_verified(username)
    assert result == "not verified"

    # Test user not found
    mock_user_collection.find_one.return_value = None
    result = check_verified(username)
    assert result == "user not found"
