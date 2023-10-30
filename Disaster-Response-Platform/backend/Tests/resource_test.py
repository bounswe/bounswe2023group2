import sys
sys.path.append("..")
from fastapi.testclient import TestClient
from main import app
from Services import resource_service 
from Models import resource_model
client = TestClient(app)

def setup_test_environment():
    """Set up test environment by creating a sample resource."""
    sample_resource = Resource(
        created_by="user123",
        condition="new",
        initialQuantity=65,
        currentQuantity=35,
        type="Cloth",
        details={
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        }
    )
    created_resource = resource_service.create_resource(sample_resource)
    return created_resource.id  # Return the ID of the created resource.

# Test for creating a resource
def test_create_resource():
    response = client.post("/", json={
        "condition": "new",
        "initialQuantity": 50,
        "currentQuantity": 20,
        "type": "Cloth",
        "details": {
            "size": "M",
            "gender": "Female",
            "age": "Adult",
            "subtype": "Shirt"
        }
    })
    assert response.status_code == 201
    data = response.json()
    assert data["condition"] == "new"
    assert data["initialQuantity"] == 50
    assert data["currentQuantity"] == 20
    assert data["type"] == "Cloth"

# Test for getting a resource
def test_get_resource():
    resource_id = setup_test_environment()
    response = client.get(f"/{resource_id}")
    assert response.status_code == 200
    data = response.json()
    assert data["id"] == resource_id
    assert data["condition"] == "new"
    assert data["initialQuantity"] == 65
    assert data["currentQuantity"] == 35
    assert data["type"] == "Cloth"

# Test for getting all resources
def test_get_all_resources():
    setup_test_environment()  # Create a sample resource
    response = client.get("/")
    assert response.status_code == 200
    data = response.json()
    assert isinstance(data, list)
    assert len(data) >= 1

# Test for updating a resource
def test_update_resource():
    resource_id = setup_test_environment()
    response = client.put(f"/{resource_id}", json={
        "created_by": "tester",
        "currentQuantity": 15,
        "details": {
            "size": "XL",
            "gender": "Male",
            "age": "Elder",
            "subtype": "Shirt"
        }
    })
    assert response.status_code == 200
    data = response.json()
    assert data["currentQuantity"] == 15
    assert data["details"]["size"] == "XL"

# Test for deleting a resource
def test_delete_resource():
    resource_id = setup_test_environment()
    response = client.delete(f"/{resource_id}")
    assert response.status_code == 200
    # Try to retrieve the deleted resource to make sure it's gone
    response = client.get(f"/{resource_id}")
    assert response.status_code == 404

# Test for setting initial quantity of a resource
def test_set_initial_quantity_of_resource():
    resource_id = setup_test_environment()
    response = client.put(f"/{resource_id}/initial_quantity", json={"quantity": 55})
    assert response.status_code == 200
    data = response.json()
    assert data["initialQuantity"] == 55

# Test for getting initial quantity of a resource
def test_get_initial_quantity_of_resource():
    resource_id = setup_test_environment()
    response = client.get(f"/{resource_id}/initial_quantity")
    assert response.status_code == 200
    data = response.json()
    assert data["initialQuantity"] == 65

# Test for setting current quantity of a resource
def test_set_current_quantity_of_resource():
    resource_id = setup_test_environment()
    response = client.put(f"/{resource_id}/current_quantity", json={"quantity": 40})
    assert response.status_code == 200
    data = response.json()
    assert data["currentQuantity"] == 40

# Test for getting current quantity of a resource
def test_get_current_quantity_of_resource():
    resource_id = setup_test_environment()
    response = client.get(f"/{resource_id}/current_quantity")
    assert response.status_code == 200
    data = response.json()
    assert data["currentQuantity"] == 35

# Test for setting condition of a resource
def test_set_condition_of_resource():
    resource_id = setup_test_environment()
    response = client.put(f"/{resource_id}/condition", json={"condition": "used"})
    assert response.status_code == 200
    data = response.json()
    assert data["condition"] == "used"

# Test for getting condition of a resource
def test_get_condition_of_resource():
    resource_id = setup_test_environment()
    response = client.get(f"/{resource_id}/condition")
    assert response.status_code == 200
    data = response.json()
    assert data["condition"] == "new"
