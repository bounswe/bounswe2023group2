import requests
from unittest import mock
import pytest
from Services.geocode_service import (
    get_address_suggestion,
    convert_address_to_coordinates,
    autocomplete_address_and_convert_to_coordinates,
    convert_coordinates_to_address,
)
from fastapi.testclient import TestClient
from Controllers.geocode_controller import router

# Mock the requests library to avoid making actual HTTP requests
@pytest.fixture
def mock_requests_get():
    with mock.patch("requests.get") as mock_get:
        yield mock_get

def test_get_address_suggestion(mock_requests_get):
    # Mock a successful response from the Google Maps Places API
    mock_response = mock.MagicMock()
    mock_response.status_code = 200
    mock_response.json.return_value = {
        "predictions": [{"description": "Test Address"}]
    }
    mock_requests_get.return_value = mock_response

    # Test getting an address suggestion
    address_suggestion = get_address_suggestion("Test Address")

    # Assert that the suggestion matches the expected value
    assert address_suggestion == "Test Address"

def test_convert_address_to_coordinates(mock_requests_get):
    # Mock a successful response from the Google Maps Geocoding API
    mock_response = mock.MagicMock()
    mock_response.status_code = 200
    mock_response.json.return_value = {
        "results": [{"geometry": {"location": {"lat": 123.456, "lng": 789.012}}}]
    }
    mock_requests_get.return_value = mock_response

    # Test converting an address to coordinates
    coordinates = convert_address_to_coordinates("Test Address")

    # Assert that the coordinates match the expected values
    assert coordinates == (123.456, 789.012)

def test_autocomplete_address_and_convert_to_coordinates(mock_requests_get):
    # Mock the get_address_suggestion function
    with mock.patch("Services.geocode_service.get_address_suggestion") as mock_get_suggestion:
        mock_get_suggestion.return_value = "Test Address"
        
        # Mock the convert_address_to_coordinates function
        with mock.patch("Services.geocode_service.convert_address_to_coordinates") as mock_convert:
            mock_convert.return_value = (123.456, 789.012)

            # Test autocompleting an address and converting it to coordinates
            coordinates = autocomplete_address_and_convert_to_coordinates("Test Address")

            # Assert that the coordinates match the expected values
            assert coordinates == (123.456, 789.012)

def test_convert_coordinates_to_address(mock_requests_get):
    # Mock a successful response from the Google Maps Geocoding API
    mock_response = mock.MagicMock()
    mock_response.status_code = 200
    mock_response.json.return_value = {
        "results": [{"formatted_address": "Test Address"}]
    }
    mock_requests_get.return_value = mock_response

    # Test converting coordinates to an address
    address = convert_coordinates_to_address(123.456, 789.012)

    # Assert that the address matches the expected value
    assert address == "Test Address"

# Mock the services used in the controller
@pytest.fixture
def mock_services():
    with mock.patch("Controllers.geocode_controller.autocomplete_address_and_convert_to_coordinates") as mock_autocomplete:
        with mock.patch("Controllers.geocode_controller.convert_coordinates_to_address") as mock_convert:
            yield mock_autocomplete, mock_convert

@pytest.fixture
def client():
    return TestClient(router)

def test_autocomplete_address(client, mock_services):
    mock_autocomplete, _ = mock_services
    mock_autocomplete.return_value = (123.456, 789.012)

    # Test autocompleting an address via the API endpoint
    response = client.get("/address_to_coordinates?address=Test%20Address")

    # Assert that the response status code is 200
    assert response.status_code == 200

    # Parse the response JSON data
    data = response.json()

    # Assert that the returned data matches the expected values
    assert data["latitude"] == 123.456
    assert data["longitude"] == 789.012
    assert data["x"] == 123.456
    assert data["y"] == 789.012

def test_convert_coordinates(client, mock_services):
    _, mock_convert = mock_services
    mock_convert.return_value = "Test Address"

    # Test converting coordinates to an address via the API endpoint
    response = client.get("/coordinates_to_address?latitude=123.456&longitude=789.012")

    # Assert that the response status code is 200
    assert response.status_code == 200

    # Parse the response JSON data
    data = response.json()

    # Assert that the returned data matches the expected values
    assert data["latitude"] == 123.456
    assert data["longitude"] == 789.012
    assert data["address"] == "Test Address"
