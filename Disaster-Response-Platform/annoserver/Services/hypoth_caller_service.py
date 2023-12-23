
import json

import requests

import hypothesis

from Models.annotations_model import Annotation, Document, Link

GLOBAL_USER = "MehmetKuzulugil"
GLOBAL_TOKEN = "6879-xDu8XO56k0EDtO28G4cCWyGlmNfNaTXmQB3B3sfABNY"
def hypothesis_store(post_data:Annotation):
    url = post_data.target[0].source
    if (url is None):
        url = post_data.target[0].url

    for iTarget in post_data.target:
        iTarget = iTarget.dict()

    link = Link(href=url, type="URL")
    if post_data.document is None:
        post_data.document = Document(title=url, link=[link])
    else:
        if post_data.document.title is None:
            post_data.document.title = url
        if post_data.document.link is None:
            post_data.document.link={link}
        if post_data.document.link[0].type is None:
            post_data.document.link[0].type = "AnyUrl"

    if post_data.references is None:
        post_data.references = []

    if (post_data.text is None):
        if (post_data.body.type =="Text"):
            post_data.text = post_data.body.value
    hypoAPIurl = "https://api.hypothes.is/api/annotations"
    headers = {"Authorization": f"Bearer {GLOBAL_TOKEN}"}
    data = json.dumps(post_data.dict())
    response = requests.request("POST", hypoAPIurl, headers=headers, data=data)
    return response
    #return r

def hypothesis_fetch(url:str, tag:str, hidden:bool):
    hypoAPIurl = "https://api.hypothes.is/api/search?user=acct:"+GLOBAL_USER+"@hypothes.is"

    if (url is not None):
        hypoAPIurl += "&uri=" + url
    if (tag is not None):
        hypoAPIurl += "&tag=" + tag
    if (hidden):
        hypoAPIurl += "&hidden=true"
    else:
        hypoAPIurl += "&hidden=false"

    headers = {"Authorization": f"Bearer {GLOBAL_TOKEN}"}

    response = requests.request("GET", hypoAPIurl, headers=headers)
    return response

def hypothesis_delete(id:str):
    hypoAPIurl = "https://api.hypothes.is/api/annotations/"

    if (id is None):
        raise ValueError("ID missing")

    hypoAPIurl += id
    headers = {"Authorization": f"Bearer {GLOBAL_TOKEN}"}

    response = requests.request("DELETE", hypoAPIurl, headers=headers)
    return response

def hypothesis_update(id:str, post_data:Annotation):
    hypoAPIurl = "https://api.hypothes.is/api/annotations/"

    if (id is None):
        raise ValueError("ID missing")

    hypoAPIurl += id
    headers = {"Authorization": f"Bearer {GLOBAL_TOKEN}"}

    data = json.dumps(post_data.dict())

    response = requests.request("PATCH", hypoAPIurl, headers=headers, data=data)
    return response
