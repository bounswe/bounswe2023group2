import unittest
from fastapi.testclient import TestClient
from main import app

class TestOnTwitter(unittest.TestCase):
    client = TestClient(app)
    def test_alive(self):
        response = self.client.get("/on-twitter/alive")
        assert response.status_code == 200
        assert response.json() == {'Alive': 'Yes'}


    def test_check(self):
        response = self.client.get("/on-twitter/check")
        assert response.status_code == 200
        assert response.json() == {'Check': 'OK'}
