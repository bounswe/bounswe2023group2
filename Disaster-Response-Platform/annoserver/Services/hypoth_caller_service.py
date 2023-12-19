import json

import requests

import hypothesis

from Models.annotations_model import W3CAnnotation, HypothesisModel

GLOBAL_TOKEN = "6879-xDu8XO56k0EDtO28G4cCWyGlmNfNaTXmQB3B3sfABNY"
def test_hypothesis_connection_get(post_data:hypothesis.HypothesisAnnotation):
    h = hypothesis.Hypothesis
    url = "https://api.hypothes.is/api/annotations"
    headers = {"Authorization": f"Bearer {GLOBAL_TOKEN}"}
    data = json.dumps(post_data.dict())
    response = requests.request("POST", url, headers=headers, data=data)
    return response

