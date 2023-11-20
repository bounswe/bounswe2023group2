from fastapi.testclient import TestClient
from main import app
from Database.mongo import MongoDB
client = TestClient(app)
db = MongoDB.getInstance()


def test_proficiency_request1():
    response = client.post("/api/userroles/signup", json=
    correct_signup_body)
    assert response.status_code == 201