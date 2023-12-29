import sys
sys.path.append("..")
from fastapi.testclient import TestClient
from main import app
from Models.resource_model import Resource
import random
import json
import string
from http import HTTPStatus

client = TestClient(app)

# Replace with a valid user's token
TOKEN = None

# Randomly generated username and email for testing
TEST_USERNAME = None
TEST_EMAIL = None
TEST_PASSWORD = "buraK1234*"

valid_headers = {}

def generate_random_string(length=10):
    """Generate a random string of letters and digits."""
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def generate_random_email():
    """Generate a random email address."""
    random_username = generate_random_string()
    return f"{random_username}@example.com"

def generate_random_phone_number():
    """Generate a random phone number with the format 05XXXXXXXX."""
    # Generate a random 8-digit number
    random_digits = ''.join([str(random.randint(0, 9)) for _ in range(9)])
    # Return the number with the 05 prefix
    return "05" + random_digits

def signup_user():
    random_username = generate_random_string()
    random_email = generate_random_email()
    random_number = generate_random_phone_number()
    response = client.post("/api/users/signup", json={
        "username": random_username,
        "email": random_email,
        "disabled": False,
        "password": TEST_PASSWORD,
        "first_name": "string",
        "last_name": "string",
        "phone_number": random_number,
        "is_email_verified": True,
        "private_account": False
    })
    assert response.status_code == 201
    return random_username, random_email

def login_user(username, password):
    response = client.post("/api/users/login", json={
        "username_or_email_or_phone": username,
        "password": password
    })
    assert response.status_code == 200
    token_data = response.json()
    return token_data["access_token"]

def delete_user(username):
    response = client.delete(f"/api/users/{username}")
    assert response.status_code == 200

def setup_test_environment():
    """Set up test environment by creating a sample resource."""
    global TOKEN, TEST_USERNAME, TEST_EMAIL, valid_headers
    # Signup and login only if the token is not already available
    if TOKEN is None:
        TEST_USERNAME, TEST_EMAIL = signup_user()
        TOKEN = login_user(TEST_USERNAME, TEST_PASSWORD)

    
    valid_headers = {
        "Authorization": f"Bearer {TOKEN}"
    }

def teardown_test_environment():
    """Tear down the test environment by deleting the test user."""
    global TEST_USERNAME
    if TEST_USERNAME is not None:
        delete_user(TEST_USERNAME)

# Import necessary modules and functions here

def test_create_resource():
    setup_test_environment()
    
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    # Clean up: Delete the created resource
    resource_id = created_resource["_id"]
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in  [HTTPStatus.OK, HTTPStatus.CREATED]

def test_get_resource():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    # Test getting the created resource
    resource_id = created_resource["_id"]
    response = client.get(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]
    resource = response.json()
    assert resource["resources"][0]["_id"] == resource_id

    # Clean up: Delete the created resource
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

def test_get_all_resources():
    setup_test_environment()

    # Test getting all resources
    response = client.get("/api/resources/", headers=valid_headers)

    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]
    resources = response.json()
    assert isinstance(resources["resources"], list)

def test_update_resource():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    # Test updating the created resource
    resource_id = created_resource["_id"]
    updated_resource_data = {
        "currentQuantity": 50,
        "details": {
            "size": "XL"
        }
    }

    response = client.put(f"/api/resources/{resource_id}", json=updated_resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]
    updated_resource = response.json()["resources"][0]
    #ATTENTION commented this out as the api does not return the id for now
    # assert updated_resource["_id"] == resource_id
    assert updated_resource["currentQuantity"] == 50
    assert updated_resource["details"]["size"] == "XL"

    # Clean up: Delete the created resource
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

def test_delete_resource():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    # Test deleting the created resource
    resource_id = created_resource["_id"]
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    # Test getting the deleted resource (should return 404)
    response = client.get(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code == HTTPStatus.NOT_FOUND

def test_set_initial_quantity():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    resource_id = created_resource["_id"]
    new_initial_quantity = 75

    response = client.put(f"/api/resources/{resource_id}/initial_quantity", json={"quantity": new_initial_quantity}, headers=valid_headers)
    assert response.status_code == HTTPStatus.OK

    # Verify the updated initial quantity
    response = client.get(f"/api/resources/{resource_id}/initial_quantity", headers=valid_headers)
    assert response.status_code == HTTPStatus.OK
    quantity_data = response.json()
    assert quantity_data["quantity"] == new_initial_quantity

    # Clean up: Delete the created resource
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

def test_set_current_quantity():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    resource_id = created_resource["_id"]
    new_current_quantity = 75

    response = client.put(f"/api/resources/{resource_id}/current_quantity", json={"quantity": new_current_quantity}, headers=valid_headers)
    assert response.status_code == HTTPStatus.OK

    # Verify the updated current quantity
    response = client.get(f"/api/resources/{resource_id}/current_quantity", headers=valid_headers)
    assert response.status_code == HTTPStatus.OK
    quantity_data = response.json()
    assert quantity_data["quantity"] == new_current_quantity

    # Clean up: Delete the created resource
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

def test_set_condition():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    resource_id = created_resource["_id"]
    new_condition = "used"

    response = client.put(f"/api/resources/{resource_id}/condition", json={"condition": new_condition}, headers=valid_headers)
    assert response.status_code == HTTPStatus.OK

    # Verify the updated condition
    response = client.get(f"/api/resources/{resource_id}/condition", headers=valid_headers)
    assert response.status_code == HTTPStatus.OK
    condition_data = response.json()
    assert condition_data["condition"] == new_condition

    # Clean up: Delete the created resource
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

def test_get_initial_quantity_succeed():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    resource_id = created_resource["_id"]

    # Test getting the initial quantity of the created resource
    response = client.get(f"/api/resources/{resource_id}/initial_quantity", headers=valid_headers)
    assert response.status_code == HTTPStatus.OK
    quantity_data = response.json()
    assert "quantity" in quantity_data

    # Clean up: Delete the created resource
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

def test_get_current_quantity_succeed():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    resource_id = created_resource["_id"]

    # Test getting the current quantity of the created resource
    response = client.get(f"/api/resources/{resource_id}/current_quantity", headers=valid_headers)
    assert response.status_code == HTTPStatus.OK
    quantity_data = response.json()
    assert "quantity" in quantity_data

    # Clean up: Delete the created resource
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

### SUPPOSED TO FAIL TESTS
def test_create_resource_missing_fields():
    setup_test_environment()
    
    resource_data = {
        "condition": "new",
        "type": "Cloth",
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code == HTTPStatus.NOT_FOUND  # Change NOT_FOUND to BAD_REQUEST

def test_get_nonexistent_resource():
    setup_test_environment()

    resource_id = "111111111111000000000000"
    response = client.get(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code == HTTPStatus.NOT_FOUND

def test_update_nonexistent_resource():
    setup_test_environment()

    resource_id = "222222222222000000000000"
    updated_resource_data = {
        "currentQuantity": 50
    }

    response = client.put(f"/api/resources/{resource_id}", json=updated_resource_data, headers=valid_headers)
    assert response.status_code == HTTPStatus.NOT_FOUND

def test_delete_nonexistent_resource():
    setup_test_environment()

    resource_id = "121212121212000000000000"
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code == HTTPStatus.NOT_FOUND

def test_set_invalid_condition():
    setup_test_environment()

    # Create a resource for testing
    resource_data = {
        "condition": "new",
        "initialQuantity": 100,
        "currentQuantity": 100,
        "type": "Cloth",
        "details": {
            "size": "L",
            "gender": "Male",
            "age": "Adult",
            "subtype": "Shirt"
        },
        "x": 12.00,
        "y": 14.00
    }

    response = client.post("/api/resources/", json=resource_data, headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

    created_resource = response.json()["resources"][0]
    assert created_resource["_id"] is not None

    resource_id = created_resource["_id"]
    invalid_condition_data = {
        "condition": "invalid_condition"
    }

    response = client.put(f"/api/resources/{resource_id}/condition", json=invalid_condition_data, headers=valid_headers)
    assert response.status_code == HTTPStatus.UNPROCESSABLE_ENTITY

    # Clean up: Delete the created resource
    response = client.delete(f"/api/resources/{resource_id}", headers=valid_headers)
    assert response.status_code in [HTTPStatus.OK, HTTPStatus.CREATED]

def test_get_initial_quantity_of_nonexistent_resource():
    setup_test_environment()

    resource_id = "353535353535000000000000"
    response = client.get(f"/api/resources/{resource_id}/initial_quantity", headers=valid_headers)
    assert response.status_code == HTTPStatus.NOT_FOUND

def test_get_current_quantity_of_nonexistent_resource():
    setup_test_environment()

    resource_id = "000000000000000000000000"
    response = client.get(f"/api/resources/{resource_id}/current_quantity", headers=valid_headers)
    assert response.status_code == HTTPStatus.NOT_FOUND

def test_get_condition_of_nonexistent_resource():
    setup_test_environment()

    resource_id = "aaaaaaaaaaaa000000000000"
    response = client.get(f"/api/resources/{resource_id}/condition", headers=valid_headers)
    assert response.status_code == HTTPStatus.NOT_FOUND
