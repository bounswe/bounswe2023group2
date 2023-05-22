import json
from fastapi.testclient import TestClient
from main import app
from database.mongo import MongoDB

client = TestClient(app)

def test_add_admin_success():
    email = "admin@example.com"
    response = client.post('/emailreport/add_admin', json={"email": email})
    assert response.status_code == 200
    assert response.json() == {'message': 'Admin added successfully'}


def test_add_admin_failure():
    email = "admin@example.com"
    response = client.post('/emailreport/add_admin', json={"email": email})
    assert response.status_code == 500


def test_get_admin_success():
    response = client.get('/emailreport/get_admin')
    assert response.status_code == 200
    expected_data = {
        "username": "admin4report",
        "first_name": "admin",
        "last_name": "istrator",
        "email": "admin@example.com",
        "phone_number": "+905554443322"
    }
    assert response.json() == expected_data


def test_get_admin_failure():
    response = client.get('/emailreport/get_admin')
    assert response.status_code == 404
    expected_data = {
        "detail": "Admin not found in the DB"
    }
    assert response.json() == expected_data


def test_drop_admin_table_success():
    response = client.delete('/emailreport/drop_admin_table')
    assert response.status_code == 200
    expected_data = {
        "message": "Admin table dropped successfully"
    }
    assert response.json() == expected_data


def test_drop_admin_table_failure():
    response = client.delete('/emailreport/drop_admin_table')
    assert response.status_code == 500


def test_emailreport_success():
    report_data = {
        "reporter": "John Doe",
        "activity": "Activity 123",
        "reason": "Spam",
        "details": "This is a test report."
    }
    response = client.post('/emailreport', json=report_data)
    assert response.status_code == 200
    assert response.json()["reporter"] == "John Doe"
    assert response.json()["activity"] == "Activity 123"
    assert response.json()["reason"] == "Spam"
    assert response.json()["details"] == "This is a test report."


def test_emailreport_failure():
    report_data = {
        "reporter": "John Doe",
        "activity": "Activity 123",
        # Missing "reason" field
        "details": "This is a test report."
    }

    response = client.post('/emailreport', json=report_data)
    assert response.status_code == 422