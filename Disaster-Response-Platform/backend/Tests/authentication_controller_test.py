from fastapi.testclient import TestClient
from main import app

client = TestClient(app)


def test_login():
    url_params = {"username": "mehmetk", "password": "CMPE451"}
    response = client.get("/api/authenticate/login", params=url_params)

<<<<<<< HEAD
=======
    print(response.json())
>>>>>>> de31b6701804379ccfc443944a45699294b772e3
    assert response.status_code == 200

    token = response.json()["SESSIONTOKEN"]

    assert token is not None
    return token


def test_create_user() -> str:
    token = test_login()
    url_params = {"username": "deprembaba", "password": "CMPE451"}
    response = client.post("/api/authenticate/create-user", json=
    url_params, headers={"Authorization": f"Bearer {token} username mehmetk"})

<<<<<<< HEAD
=======
    print(response.json())
>>>>>>> de31b6701804379ccfc443944a45699294b772e3
    assert response.status_code == 200

    url_params = {"username": "deprembaba"}
    response = client.delete("/api/authenticate/delete-user", params=url_params,
                             headers={"Authorization": f"Bearer {token} username mehmetk"})
<<<<<<< HEAD
=======

    print(response.json())
>>>>>>> de31b6701804379ccfc443944a45699294b772e3
    assert response.status_code == 200
    return token


test_create_user()
