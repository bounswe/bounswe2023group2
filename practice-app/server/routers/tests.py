import unittest
import requests
import re

class ResourceTests(unittest.TestCase):
    def testItReturns200(self):
        r = requests.post(f'http://0.0.0.0:8000/word/hasan', json={
            "notes": "Merhaba"
        })
        # test response uuid
        self.assertEqual(r.status_code, 200)
    def testItRequiresCorrectParameters(self):
        r = requests.post(f'http://0.0.0.0:8000/word/hasan', json={
            "nothing": "nothing"
        })
        # test response uuid
        self.assertNotEqual(r.status_code, 200)