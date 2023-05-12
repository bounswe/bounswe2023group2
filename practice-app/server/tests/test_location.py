from fastapi.testclient import TestClient

from fastapi.testclient import TestClient

from main import app

client = TestClient(app)

def test_locations_fetch():
    response = client.get("/location/")
    assert response.status_code == 200
    assert response.json() == [
        {'username': 'ali', 'x_coord': 37.08328368718349, 'y_coord': 36.83810055490806},
        {'username': 'kemal', 'x_coord': 38.934396015417725, 'y_coord': 38.32618261611021},
        {'username': 'ayşe', 'x_coord': 39.915131681651594, 'y_coord': 37.35510170315383}
        ]

def test_locations_insert():
    response = client.post("/location/insert",json={"items": [{'x_coord': 38.64261790634527, 'y_coord': 37.27661132812501}]})
    assert response.status_code == 200
    assert response.json() == [
        {'x_coord': '38.64261790634527', 'y_coord': '37.27661132812501'}
        ]
