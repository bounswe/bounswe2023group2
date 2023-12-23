from Database.mongo import MongoDB
from fastapi.security import OAuth2PasswordBearer
from datetime import *
from fastapi import HTTPException, Depends, status
from jose import JWTError, jwt
from passlib.context import CryptContext
from Models.user_model import *
from Models.resource_model import *
from Models.need_model import *
from Models.action_model import Action
from Models.search_model import *
from Models.event_model import Event
from typing import Annotated
import config
import requests
from bson import ObjectId
#roleRequestsDb = MongoDB.get_collection('role_verification_requests')
userDb = MongoDB.get_collection('authenticated_user')
resource_collection = MongoDB.get_collection('resources')
need_collection = MongoDB.get_collection('needs')
action_collection = MongoDB.get_collection('actions')
events_collection = MongoDB.get_collection('events')

def translate(query):
    url = "https://google-translate-api8.p.rapidapi.com/google-translate/"

    querystring = {"text":{query},"lang":"en"}
    headers = {
        "content-type": "application/json",
        "X-RapidAPI-Key": config.X_RAPIDAPI_KEY, #bunları config e taşı
        "X-RapidAPI-Host": "google-translate-api8.p.rapidapi.com"
    }

    response = requests.post(url, headers=headers, params=querystring)
    if response.status_code == 200:
        data = response.json()
        query= data['result']
    return query
        
def convert_objectid(doc):
    if isinstance(doc, list):
        return [convert_objectid(item) for item in doc]
    elif isinstance(doc, dict):
        for key, value in doc.items():
            if isinstance(value, ObjectId):
                doc[key] = str(value)
            else:
                doc[key] = convert_objectid(value)
        return doc
    else:
        return doc
def process_and_fetch_users(query, field_name, list, type):
  
        if not list:
            return []
        url = "http://209.38.180.66:9060/relevance"
        body = RelevanceBody(search_text=query, search_list=list)
        response = requests.post(url, json=body.dict())
        response.raise_for_status()
        relevances = response.json()["relevances"]

        name_relevance_pairs = zip(list, relevances)
        filtered_pairs = filter(lambda x: x[1] > 0.15, name_relevance_pairs)
        sorted_pairs = sorted(filtered_pairs, key=lambda x: x[1], reverse=True)

        relevant_names = [name for name, _ in sorted_pairs]

        if type==0:        
            relevant_users = userDb.find({field_name: {"$in": relevant_names}})
            return [UserInfo(**convert_objectid(user)) for user in relevant_users]
        elif type==1 :
            if field_name=="details_type":
                relevant_resources = resource_collection.find({"details.subtype": {"$in": relevant_names}})
            else:
                relevant_resources= resource_collection.find({field_name: {"$in": relevant_names}})
                relevant_resources = sorted(relevant_resources, key=lambda x: relevant_names.index(x[field_name]))

            return [Resource(**resource) for resource in relevant_resources]
        elif type==2:
            if field_name=="details_type":
                relevant_needs = need_collection.find({"details.subtype": {"$in": relevant_names}})
            else:
                relevant_needs= need_collection.find({field_name: {"$in": relevant_names}})
                relevant_needs = sorted(relevant_needs, key=lambda x: relevant_names.index(x[field_name]))
            return [Need(**need) for need in relevant_needs]   
        elif type==3:
            
            relevant_actions= action_collection.find({field_name: {"$in": relevant_names}})
            relevant_actions = sorted(relevant_actions, key=lambda x: relevant_names.index(x[field_name]))
            return [Action(**action) for action in relevant_actions] 
        elif type==4:
            relevant_events= events_collection.find({field_name: {"$in": relevant_names}})
            relevant_events = sorted(relevant_events, key=lambda x: relevant_names.index(x[field_name]))
            return [Event(**event) for event in relevant_events] 


def search_users(query: str)-> List[dict]:
    #if username is given
    cursor = userDb.find({
        "$or": [
            {"username": {"$regex": query, "$options": "i"}},
            {"email": {"$regex": query, "$options": "i"}},
            {"first_name": {"$regex": query, "$options": "i"}}
        ]
    })
    
    
    results = [UserInfo(**document) for document in cursor]
    if results:
        return SearchList(results=results)
    try:
        users = userDb.find({}, {"_id": 0, "username": 1, "first_name": 1}).limit(100)
        users= list(users)
      
        first_names = [user["first_name"] for user in users]
        top_users_list = process_and_fetch_users(query,"first_name", first_names, 0)
        #top_users_list = process_and_fetch_users("first_name", first_names)     
        usernames = [user["username"] for user in users]
        if not top_users_list:
            top_users_list = process_and_fetch_users(query,"username", usernames, 0)
            #top_users_list = process_and_fetch_users("username", usernames)
        return SearchList(results=top_users_list) 
    except requests.RequestException as e:
        # Handle any exceptions that occur during the request
        raise HTTPException(status_code=400, detail=str(e))

def search_resources(query: str)-> List[dict]:
    #if username is given
    cursor = resource_collection.find({
        "$or": [
            {"created_by": {"$regex": query, "$options": "i"}},
            {"description": {"$regex": query, "$options": "i"}},
            {"type": {"$regex": query, "$options": "i"}},
            {"details.subtype": {"$regex": query, "$options": "i"}}
        ]
    })
    results = [Resource(**document) for document in cursor]
    if results:
        return SearchList(results=results)
    try:
        query= translate(query)

        resources = resource_collection.find({}, {"_id": 0, "description": 1, "type": 1, "details.subtype":1}).limit(100)
        resources= list(resources)
        detail_types = [resource.get("details", {}).get("subtype") for resource in resources if resource.get("details", {}).get("subtype") is not None]
        top_resources_list = process_and_fetch_users(query,"details_type", detail_types , 1)
 
        if not top_resources_list:
            descriptions = types = [resource["description"] for resource in resources if resource.get("description") is not None]
            top_resources_list = process_and_fetch_users(query,"description", descriptions , 1) 
   
            if not top_resources_list:
                types = [resource["type"] for resource in resources]
                top_resources_list = process_and_fetch_users(query,"type", types, 1)  
      
        return SearchList(results=top_resources_list) 
    except requests.RequestException as e:
        raise HTTPException(status_code=400, detail=str(e)) 

    

def search_needs(query: str)-> List[dict]:
    #if username is given
    cursor = need_collection.find({
        "$or": [
            {"created_by": {"$regex": query, "$options": "i"}},
            {"description": {"$regex": query, "$options": "i"}},
            {"type": {"$regex": query, "$options": "i"}},
            {"details.subtype": {"$regex": query, "$options": "i"}}
        ]
    })
    results = [Need(**document) for document in cursor]
    if results:
        return SearchList(results=results)
    try:
        #query= translate(query)

        needs = need_collection.find({}, {"_id": 0, "description": 1, "type": 1, "details.subtype":1}).limit(100)
        needs= list(needs)
        detail_types = [need.get("details", {}).get("subtype") for need in needs if need.get("details", {}).get("subtype") is not None]
        top_needs_list = process_and_fetch_users(query,"details_type", detail_types , 2)
        if not top_needs_list:
            descriptions = types = [need["description"] for need in needs if need.get("description") is not None]
            top_needs_list = process_and_fetch_users(query,"description", descriptions , 2)  
            if not top_needs_list:
                types = [need["type"] for need in needs]
                top_needs_list = process_and_fetch_users(query,"type", types, 2)  
        return SearchList(results=top_needs_list) 
    except requests.RequestException as e:
        raise HTTPException(status_code=400, detail=str(e)) 

def search_actions(query: str)-> List[dict]:
    #if username is given
    cursor = action_collection.find({
        "$or": [
            {"type": {"$regex": query, "$options": "i"}},
            {"description": {"$regex": query, "$options": "i"}}
        ]
    })
    
    
    results = [Action(**document) for document in cursor]
    if results:
        return SearchList(results=results)
    try:
        actions = action_collection.find({}, {"_id": 0, "type": 1, "description": 1}).limit(100)
        actions= list(actions)
        descriptions = [action["description"] for action in actions if action.get("description") is not None]
        top_actions_list = process_and_fetch_users(query,"description", descriptions , 3)  
        if not top_actions_list:
            types = [action["type"] for action in actions]
            top_actions_list = process_and_fetch_users(query,"type", types, 3)  
        return SearchList(results=top_actions_list) 
    
    except requests.RequestException as e:
        
        raise HTTPException(status_code=400, detail=str(e))

def search_events(query: str)-> List[dict]:
    cursor = events_collection.find({
        "$or": [
            {"event_type": {"$regex": query, "$options": "i"}},
            {"short_description": {"$regex": query, "$options": "i"}}
        ]
    })
    
    results = [Event(**document) for document in cursor]
    if results:
        return SearchList(results=results)
    try:
        events = events_collection.find({}, {"_id": 0, "event_type": 1, "short_description": 1}).limit(100)
        events= list(events)
  
        descriptions = [event["short_description"] for event in events if event.get("short_description") is not None]
        top_events_list = process_and_fetch_users(query,"short_description", descriptions , 4)  
        if not top_events_list:
            types = [event["event_type"] for event in events if event.get("event_type") is not None]
            top_events_list = process_and_fetch_users(query,"event_type", types, 4)  
        return SearchList(results=top_events_list) 
    
    except requests.RequestException as e:
        
        raise HTTPException(status_code=400, detail=str(e))