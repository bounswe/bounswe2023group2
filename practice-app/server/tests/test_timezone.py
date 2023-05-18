import unittest
import pytest
from fastapi.testclient import TestClient
from main import app

class TimezoneTests(unittest.TestCase):
    client = TestClient(app)
    
    def test_get_timezone(self):
        response = self.client.get("/timezone/get_timezone", json={
            "latitude": 122,
            "longitude": 32,
        })
        self.assertEqual(response.status_code, 200)

    @pytest.mark.parametrize(
        "expected_status_code",
        [(200)],
    )
    def test_list_timezones(self, expected_status_code):
        response = self.client.get("/tz_conversion/list_timezones")
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
    def test_convert_time(self, time, from_tz, to_tz, expected_status_code):
        response = self.client.post(
            "/tz_conversion/convert_time", json={"time": time, "from_tz": from_tz, "to_tz": to_tz}
        )
        assert response.status_code == expected_status_code

    @pytest.mark.parametrize(
        "expected_status_code",
        [(200)],
    )
    def test_get_saved_conversions(self, expected_status_code):
        response = self.client.get("/tz_conversion/saved_conversions")
        assert response.status_code == expected_status_code

    @pytest.mark.parametrize(
        "conversion_name, expected_status_code",
        [
            ("Invalid name", 404),
        ],
    )
    def test_delete_conversion(self, conversion_name, expected_status_code):
        response = self.client.post("/tz_conversion/delete_conversion",
                               json={"conversion_name": conversion_name})
        assert response.status_code == expected_status_code
