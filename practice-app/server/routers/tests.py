import unittest
import requests
import re
from config import Config
import pytest
from fastapi.testclient import TestClient
from app.main import app

class ResourceTests(unittest.TestCase):
    def testItReturns200(self):
        r = requests.post(f'{Config.BACKEND_URL}/timezone/get_timezone', json={
            "notes": "Merhaba"
        })
        self.assertEqual(r.status_code, 200)

    def testItRequiresCorrectParameters(self):
        r = requests.post(f'{Config.BACKEND_URL}/timezone/get_timezone', json={
            "nothing": "nothing"
        })
        self.assertNotEqual(r.status_code, 200)

client = TestClient(app)


@pytest.mark.parametrize(
    "expected_status_code",
    [(200)],
)
def test_list_timezones(expected_status_code):
    response = client.get("/tz_conversion/list_timezones")
    assert response.status_code == expected_status_code


@pytest.mark.parametrize(
    "time, from_tz, to_tz, expected_status_code",
    [
        ("2023-05-12 15:00:00", "America/New_York", "Europe/London", 200),
        ("2023-05-12 15:00:00", "Invalid timezone", "Europe/London", 400),
        ("2023-05-12 15:00:00", "America/New_York", "Invalid timezone", 400),
        ("2023-05-12T15:00:00", "America/New_York", "Europe/London", 400),
    ],
)
def test_convert_time(time, from_tz, to_tz, expected_status_code):
    response = client.post(
        "/tz_conversion/convert_time", json={"time": time, "from_tz": from_tz, "to_tz": to_tz}
    )
    assert response.status_code == expected_status_code


@pytest.mark.parametrize(
    "expected_status_code",
    [(200)],
)
def test_get_saved_conversions(expected_status_code):
    response = client.get("/tz_conversion/saved_conversions")
    assert response.status_code == expected_status_code


@pytest.mark.parametrize(
    "conversion_name, expected_status_code",
    [
        ("Invalid name", 404),
    ],
)
def test_delete_conversion(conversion_name, expected_status_code):
    response = client.post("/tz_conversion/delete_conversion",
                           json={"conversion_name": conversion_name})
    assert response.status_code == expected_status_code
