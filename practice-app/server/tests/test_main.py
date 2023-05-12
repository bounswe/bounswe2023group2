from fastapi.testclient import TestClient

from fastapi.testclient import TestClient

from main import app

client = TestClient(app)


def test_read_main():
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {"msg": "Hello World"}

def test_read_item():
    response = client.get("/user/ali", )
    assert response.status_code == 200
    assert response.json() == {
        "username": "ali",
    }

