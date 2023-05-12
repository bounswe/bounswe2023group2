import unittest
import requests
import re
from const import *
import pytest
from fastapi.testclient import TestClient
from app.main import app

class ResourceTests(unittest.TestCase):

    # Add Tests
    def test_full_arguments(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
        uuid = r.text[1:-1]
        self.assertTrue(uuid4regex.match(uuid), f'uuid {uuid} does not match regex {uuid4regex.pattern}')
        return uuid

    def test_without_optionals(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
        uuid = r.text[1:-1]
        self.assertTrue(uuid4regex.match(uuid), f'uuid {uuid} does not match regex {uuid4regex.pattern}')

    def test_type_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_location_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_updated_at_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_is_active_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_upvotes_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_downvotes_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_creator_id_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_creation_date_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "condition": "good",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_condition_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_quantity_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/resource', json={
            "type": "food",
            "location": "1234",
            "notes": "this resource is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "condition": "good",
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    # Get Tests
    def test_get(self):
        uuid = self.test_full_arguments()
        # get the resource
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/resource/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        self.assertEqual(data['type'], 'food')
        self.assertEqual(data['location'], '1234')
        self.assertEqual(data['notes'], 'this resource is here for a test')
        self.assertEqual(data['updated_at'], '2020-04-01T12:00:00Z')
        self.assertEqual(data['is_active'], True)
        self.assertEqual(data['upvotes'], 0)
        self.assertEqual(data['downvotes'], 0)
        self.assertEqual(data['creator_id'], 'test')
        self.assertEqual(data['creation_date'], '2020-04-01T12:00:00Z')
        self.assertEqual(data['condition'], 'good')
        self.assertEqual(data['quantity'], 1)

    def test_no_get(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/resource/?id=00000000-0000-4000-a000-000000000000')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, "null")

    def test_0_resources_list(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/resource')
        self.assertEqual(r.status_code, 200)

    def test_3_resources_list(self):
        uuid1 = self.test_full_arguments()
        uuid2 = self.test_full_arguments()
        uuid3 = self.test_full_arguments()
        # get the list
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/resource')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        self.assertGreaterEqual(len(data), 3)
        ids = [0, 0, 0]
        for i in range(len(data)):
            if data[i]['id'] == uuid1:
                ids[0] = i
                break
        else:
            self.assertTrue(False)
        for i in range(len(data)):
            if data[i]['id'] == uuid2:
                ids[1] = i
                break
        else:
            self.assertTrue(False)
        for i in range(len(data)):
            if data[i]['id'] == uuid3:
                ids[2] = i
                break
        else:
            self.assertTrue(False)
        for i in ids:
            self.assertEqual(data[i]['type'], 'food')
            self.assertEqual(data[i]['location'], '1234')
            self.assertEqual(data[i]['notes'], 'this resource is here for a test')
            self.assertEqual(data[i]['updated_at'], '2020-04-01T12:00:00Z')
            self.assertEqual(data[i]['is_active'], True)
            self.assertEqual(data[i]['upvotes'], 0)
            self.assertEqual(data[i]['downvotes'], 0)
            self.assertEqual(data[i]['creator_id'], 'test')
            self.assertEqual(data[i]['creation_date'], '2020-04-01T12:00:00Z')
            self.assertEqual(data[i]['condition'], 'good')
            self.assertEqual(data[i]['quantity'], 1)

    # Delete Tests
    def test_delete_first(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/resource')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        uuid = data[0]['id']
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/resource/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, '"Successfully deleted"')
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/resource/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, "null")

    def test_delete_unpresent(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/resource/?id=00000000-0000-4000-a000-000000000000')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, '"Resource not found"')

class NeedTests(unittest.TestCase):

    # Add Tests
    def test_full_arguments(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
        uuid = r.text[1:-1]
        self.assertTrue(uuid4regex.match(uuid), f'uuid {uuid} does not match regex {uuid4regex.pattern}')
        return uuid

    def test_without_optionals(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
        uuid = r.text[1:-1]
        self.assertTrue(uuid4regex.match(uuid), f'uuid {uuid} does not match regex {uuid4regex.pattern}')

    def test_type_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_location_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_updated_at_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_is_active_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_upvotes_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_downvotes_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_creator_id_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_creation_date_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "urgency": 5,
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_urgency_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "quantity": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_quantity_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/need', json={
            "type": "food",
            "location": "1234",
            "notes": "this need is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "urgency": 5,
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    # Get Tests
    def test_get(self):
        uuid = self.test_full_arguments()
        # get the need
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/need/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        self.assertEqual(data['type'], 'food')
        self.assertEqual(data['location'], '1234')
        self.assertEqual(data['notes'], 'this need is here for a test')
        self.assertEqual(data['updated_at'], '2020-04-01T12:00:00Z')
        self.assertEqual(data['is_active'], True)
        self.assertEqual(data['upvotes'], 0)
        self.assertEqual(data['downvotes'], 0)
        self.assertEqual(data['creator_id'], 'test')
        self.assertEqual(data['creation_date'], '2020-04-01T12:00:00Z')
        self.assertEqual(data['urgency'], 5)
        self.assertEqual(data['quantity'], 1)

    def test_no_get(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/need/?id=00000000-0000-4000-a000-000000000000')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, "null")

    def test_0_need(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/need')
        self.assertEqual(r.status_code, 200)

    def test_3_need(self):
        uuid1 = self.test_full_arguments()
        uuid2 = self.test_full_arguments()
        uuid3 = self.test_full_arguments()
        # get the list
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/need')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        self.assertGreaterEqual(len(data), 3)
        ids = [0, 0, 0]
        for i in range(len(data)):
            if data[i]['id'] == uuid1:
                ids[0] = i
                break
        else:
            self.assertTrue(False)
        for i in range(len(data)):
            if data[i]['id'] == uuid2:
                ids[1] = i
                break
        else:
            self.assertTrue(False)
        for i in range(len(data)):
            if data[i]['id'] == uuid3:
                ids[2] = i
                break
        else:
            self.assertTrue(False)
        for i in ids:
            self.assertEqual(data[i]['type'], 'food')
            self.assertEqual(data[i]['location'], '1234')
            self.assertEqual(data[i]['notes'], 'this need is here for a test')
            self.assertEqual(data[i]['updated_at'], '2020-04-01T12:00:00Z')
            self.assertEqual(data[i]['is_active'], True)
            self.assertEqual(data[i]['upvotes'], 0)
            self.assertEqual(data[i]['downvotes'], 0)
            self.assertEqual(data[i]['creator_id'], 'test')
            self.assertEqual(data[i]['creation_date'], '2020-04-01T12:00:00Z')
            self.assertEqual(data[i]['urgency'], 5)
            self.assertEqual(data[i]['quantity'], 1)

    # Delete Tests
    def test_delete_first(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/need')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        uuid = data[0]['id']
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/need/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, '"Successfully deleted"')
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/need/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, "null")

    def test_delete_unpresent(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/need/?id=00000000-0000-4000-a000-000000000000')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, '"Need not found"')

class EventTests(unittest.TestCase):

    # Add Tests
    def test_full_arguments(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
        uuid = r.text[1:-1]
        self.assertTrue(uuid4regex.match(uuid), f'uuid {uuid} does not match regex {uuid4regex.pattern}')
        return uuid

    def test_without_optionals(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
        uuid = r.text[1:-1]
        self.assertTrue(uuid4regex.match(uuid), f'uuid {uuid} does not match regex {uuid4regex.pattern}')

    def test_type_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "location": "1234",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_location_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_updated_at_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "notes": "this event is here for a test",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_is_active_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_upvotes_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_downvotes_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_creator_id_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creation_date": "2020-04-01T12:00:00Z",
            "duration": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_creation_date_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "duration": 1
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_duration_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/event', json={
            "type": "food",
            "location": "1234",
            "notes": "this event is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    # Get Tests
    def test_get(self):
        uuid = self.test_full_arguments()
        # get the event
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/event/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        self.assertEqual(data['type'], 'food')
        self.assertEqual(data['location'], '1234')
        self.assertEqual(data['notes'], 'this event is here for a test')
        self.assertEqual(data['updated_at'], '2020-04-01T12:00:00Z')
        self.assertEqual(data['is_active'], True)
        self.assertEqual(data['upvotes'], 0)
        self.assertEqual(data['downvotes'], 0)
        self.assertEqual(data['creator_id'], 'test')
        self.assertEqual(data['creation_date'], '2020-04-01T12:00:00Z')
        self.assertEqual(data['duration'], 1)

    def test_no_get(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/event/?id=00000000-0000-4000-a000-000000000000')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, "null")

    def test_0_event(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/event')
        self.assertEqual(r.status_code, 200)

    def test_3_event(self):
        uuid1 = self.test_full_arguments()
        uuid2 = self.test_full_arguments()
        uuid3 = self.test_full_arguments()
        # get the list
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/event')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        self.assertGreaterEqual(len(data), 3)
        ids = [0, 0, 0]
        for i in range(len(data)):
            if data[i]['id'] == uuid1:
                ids[0] = i
                break
        else:
            self.assertTrue(False)
        for i in range(len(data)):
            if data[i]['id'] == uuid2:
                ids[1] = i
                break
        else:
            self.assertTrue(False)
        for i in range(len(data)):
            if data[i]['id'] == uuid3:
                ids[2] = i
                break
        else:
            self.assertTrue(False)
        for i in ids:
            self.assertEqual(data[i]['type'], 'food')
            self.assertEqual(data[i]['location'], '1234')
            self.assertEqual(data[i]['notes'], 'this event is here for a test')
            self.assertEqual(data[i]['updated_at'], '2020-04-01T12:00:00Z')
            self.assertEqual(data[i]['is_active'], True)
            self.assertEqual(data[i]['upvotes'], 0)
            self.assertEqual(data[i]['downvotes'], 0)
            self.assertEqual(data[i]['creator_id'], 'test')
            self.assertEqual(data[i]['creation_date'], '2020-04-01T12:00:00Z')
            self.assertEqual(data[i]['duration'], 1)

    # Delete Tests
    def test_delete_first(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/event')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        uuid = data[0]['id']
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/event/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, '"Successfully deleted"')
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/event/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, "null")

    def test_delete_unpresent(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/event/?id=00000000-0000-4000-a000-000000000000')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, '"Event not found"')

class ActionTests(unittest.TestCase):

    # Add Tests
    def test_full_arguments(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "end_location": "5678",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
        uuid = r.text[1:-1]
        self.assertTrue(uuid4regex.match(uuid), f'uuid {uuid} does not match regex {uuid4regex.pattern}')
        return uuid

    def test_without_optionals(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "end_location": "5678",
            "status": "in progress",
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
        uuid = r.text[1:-1]
        self.assertTrue(uuid4regex.match(uuid), f'uuid {uuid} does not match regex {uuid4regex.pattern}')

    def test_updated_at_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "end_location": "5678",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_is_active_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "end_location": "5678",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_upvotes_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "end_location": "5678",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_downvotes_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "end_location": "5678",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_creator_id_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "end_location": "5678",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_creation_date_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "start_location": "1234",
            "end_location": "5678",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_start_location_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "end_location": "5678",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_end_location_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "status": "in progress",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    def test_status_omitted(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/add/action', json={
            "notes": "this action is here for a test",
            "updated_at": "2020-04-01T12:00:00Z",
            "is_active": True,
            "upvotes": 0,
            "downvotes": 0,
            "creator_id": "test",
            "creation_date": "2020-04-01T12:00:00Z",
            "start_location": "1234",
            "end_location": "5678",
            "used_resources": "none",
            "created_resources": "none",
            "fulfilled_needs": "none",
            "emerged_needs": "none",
            "related_events": "none"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)

    # Get Tests
    def test_get(self):
        uuid = self.test_full_arguments()
        # get the action
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/action/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        self.assertEqual(data['notes'], 'this action is here for a test')
        self.assertEqual(data['updated_at'], '2020-04-01T12:00:00Z')
        self.assertEqual(data['is_active'], True)
        self.assertEqual(data['upvotes'], 0)
        self.assertEqual(data['downvotes'], 0)
        self.assertEqual(data['creator_id'], 'test')
        self.assertEqual(data['creation_date'], '2020-04-01T12:00:00Z')
        self.assertEqual(data['start_location'],  "1234")
        self.assertEqual(data['end_location'],  "5678")
        self.assertEqual(data['status'],  "in progress")
        self.assertEqual(data['used_resources'],  "none")
        self.assertEqual(data['created_resources'],  "none")
        self.assertEqual(data['fulfilled_needs'],  "none")
        self.assertEqual(data['emerged_needs'],  "none")
        self.assertEqual(data['related_events'],  "none")


    def test_no_get(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/action/?id=00000000-0000-4000-a000-000000000000')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, "null")

    def test_0_action(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/action')
        self.assertEqual(r.status_code, 200)

    def test_3_action(self):
        uuid1 = self.test_full_arguments()
        uuid2 = self.test_full_arguments()
        uuid3 = self.test_full_arguments()
        # get the list
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/action')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        self.assertGreaterEqual(len(data), 3)
        ids = [0, 0, 0]
        for i in range(len(data)):
            if data[i]['id'] == uuid1:
                ids[0] = i
                break
        else:
            self.assertTrue(False)
        for i in range(len(data)):
            if data[i]['id'] == uuid2:
                ids[1] = i
                break
        else:
            self.assertTrue(False)
        for i in range(len(data)):
            if data[i]['id'] == uuid3:
                ids[2] = i
                break
        else:
            self.assertTrue(False)
        for i in ids:
            self.assertEqual(data[i]['notes'], 'this action is here for a test')
            self.assertEqual(data[i]['updated_at'], '2020-04-01T12:00:00Z')
            self.assertEqual(data[i]['is_active'], True)
            self.assertEqual(data[i]['upvotes'], 0)
            self.assertEqual(data[i]['downvotes'], 0)
            self.assertEqual(data[i]['creator_id'], 'test')
            self.assertEqual(data[i]['creation_date'], '2020-04-01T12:00:00Z')
            self.assertEqual(data[i]['start_location'],  "1234")
            self.assertEqual(data[i]['end_location'],  "5678")
            self.assertEqual(data[i]['status'],  "in progress")
            self.assertEqual(data[i]['used_resources'],  "none")
            self.assertEqual(data[i]['created_resources'],  "none")
            self.assertEqual(data[i]['fulfilled_needs'],  "none")
            self.assertEqual(data[i]['emerged_needs'],  "none")
            self.assertEqual(data[i]['related_events'],  "none")

    # Delete Tests
    def test_delete_first(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/action')
        self.assertEqual(r.status_code, 200)
        data = r.json()
        uuid = data[0]['id']
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/action/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, '"Successfully deleted"')
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/get/action/?id={uuid}')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, "null")

    def test_delete_unpresent(self):
        r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/action/?id=00000000-0000-4000-a000-000000000000')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, '"Action not found"')

class ZZZCleanup(unittest.TestCase):
    def test_zzz_cleanup(self):
        data = []
        data += requests.get(f'http://0.0.0.0:8000{api_prefix}/get/need').json()
        data += requests.get(f'http://0.0.0.0:8000{api_prefix}/get/resource').json()
        data += requests.get(f'http://0.0.0.0:8000{api_prefix}/get/action').json()
        data += requests.get(f'http://0.0.0.0:8000{api_prefix}/get/event').json()
        for activity in data:
            if activity['updated_at'] == '2020-04-01T12:00:00Z':
                r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/action/?id={activity["id"]}')
                r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/need/?id={activity["id"]}')
                r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/resource/?id={activity["id"]}')
                r = requests.get(f'http://0.0.0.0:8000{api_prefix}/delete/event/?id={activity["id"]}')


class TimezoneTests(unittest.TestCase):
    def test_get_timezone(self):
        r = requests.post(f'http://0.0.0.0:8000{api_prefix}/timezone/get_timezone', json={
            "latitude": "122",
            "longitude": "232",
        })
        self.assertEqual(r.status_code, 200)

    @pytest.mark.parametrize(
        "expected_status_code",
        [(200)],
    )
    def test_list_timezones(expected_status_code):
        client = TestClient(app)
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
        client = TestClient(app)
        response = client.post(
            "/tz_conversion/convert_time", json={"time": time, "from_tz": from_tz, "to_tz": to_tz}
        )
        assert response.status_code == expected_status_code


    @pytest.mark.parametrize(
        "expected_status_code",
        [(200)],
    )
    def test_get_saved_conversions(expected_status_code):
        client = TestClient(app)
        response = client.get("/tz_conversion/saved_conversions")
        assert response.status_code == expected_status_code


    @pytest.mark.parametrize(
        "conversion_name, expected_status_code",
        [
            ("Invalid name", 404),
        ],
    )
    def test_delete_conversion(conversion_name, expected_status_code):
        client = TestClient(app)
        response = client.post("/tz_conversion/delete_conversion",
                            json={"conversion_name": conversion_name})
        assert response.status_code == expected_status_code


if __name__ == '__main__':
    global uuid4regex
    uuid4regex = re.compile('[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}\Z', re.I)
    unittest.main()
