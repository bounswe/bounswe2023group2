# This file simulates a database for the application.
# Later this will be replaced by a real database.

import json
import os

def add_resource(uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, condition, quantity):
    data = {
            "uuid": uuid,
            "type": type,
            "location": location,
            "notes": notes,
            "updated_at": updated_at,
            "is_active": is_active,
            "upvotes": upvotes,
            "downvotes": downvotes,
            "creator_id": creator_id,
            "creation_date": creation_date,
            "condition": condition,
            "quantity": quantity
            }
    with open(f'data/resource/{uuid}', 'w') as f:
        json.dump(data, f)

def add_need(uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, urgency, quantity):
    data = {
            "uuid": uuid,
            "type": type,
            "location": location,
            "notes": notes,
            "updated_at": updated_at,
            "is_active": is_active,
            "upvotes": upvotes,
            "downvotes": downvotes,
            "creator_id": creator_id,
            "creation_date": creation_date,
            "urgency": urgency,
            "quantity": quantity
            }
    with open(f'data/need/{uuid}', 'w') as f:
        json.dump(data, f)

def add_event(uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, duration):
    data = {
            "uuid": uuid,
            "type": type,
            "location": location,
            "notes": notes,
            "updated_at": updated_at,
            "is_active": is_active,
            "upvotes": upvotes,
            "downvotes": downvotes,
            "creator_id": creator_id,
            "creation_date": creation_date,
            "duration": duration
            }
    with open(f'data/event/{uuid}', 'w') as f:
        json.dump(data, f)

def add_action(uuid, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, start_location, end_location, status, used_resources, created_resources, fulfilled_needs, emerged_needs, related_events):
    data = {
            "uuid": uuid,
            "notes": notes,
            "updated_at": updated_at,
            "is_active": is_active,
            "upvotes": upvotes,
            "downvotes": downvotes,
            "creator_id": creator_id,
            "creation_date": creation_date,
            "start_location": start_location,
            "end_location": end_location,
            "status": status,
            "used_resources": used_resources,
            "created_resources": created_resources,
            "fulfilled_needs": fulfilled_needs,
            "emerged_needs": emerged_needs,
            "related_events": related_events
            }
    with open(f'data/action/{uuid}', 'w') as f:
        json.dump(data, f)

def get_resource(uuid):
    try:
        with open(f'data/resource/{uuid}', 'r') as f:
            data = json.load(f)
            return data
    except:
        return None

def list_resource():
    datas = []
    for file in os.listdir('data/resource'):
        with open(f'data/resource/{file}', 'r') as f:
            datas.append(json.load(f))
    return datas

def get_need(uuid):
    try:
        with open(f'data/need/{uuid}', 'r') as f:
            data = json.load(f)
            return data
    except:
        return None

def list_need():
    datas = []
    for file in os.listdir('data/need'):
        with open(f'data/need/{file}', 'r') as f:
            datas.append(json.load(f))
    return datas

def get_event(uuid):
    try:
        with open(f'data/event/{uuid}', 'r') as f:
            data = json.load(f)
            return data
    except:
        return None

def list_event():
    datas = []
    for file in os.listdir('data/event'):
        with open(f'data/event/{file}', 'r') as f:
            datas.append(json.load(f))
    return datas

def get_action(uuid):
    try:
        with open(f'data/action/{uuid}', 'r') as f:
            data = json.load(f)
            return data
    except:
        return None

def list_action():
    datas = []
    for file in os.listdir('data/action'):
        with open(f'data/action/{file}', 'r') as f:
            datas.append(json.load(f))
    return datas

def delete_resource(id):
    if os.path.exists(f'data/resource/{id}'):
        os.remove(f'data/resource/{id}')
        return "Successfully deleted"
    return "Resource not found"

def delete_need(id):
    if os.path.exists(f'data/need/{id}'):
        os.remove(f'data/need/{id}')
        return "Successfully deleted"
    return "Need not found"

def delete_event(id):
    if os.path.exists(f'data/event/{id}'):
        os.remove(f'data/event/{id}')
        return "Successfully deleted"
    return "Event not found"

def delete_action(id):
    if os.path.exists(f'data/action/{id}'):
        os.remove(f'data/action/{id}')
        return "Successfully deleted"
    return "Action not found"

