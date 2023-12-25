from Models.resource_model import ConditionEnum
from Models.action_model_v2 import *
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from bson import json_util
from datetime import datetime, timezone, timedelta
from Services.build_API_returns import *
from Services.resource_service import *
from Services.need_service import *

# Get the resources collection using the MongoDB class
actions_collection = MongoDB.get_collection('actions')
resources_collection= MongoDB.get_collection('resources')
users_collection= MongoDB.get_collection('authenticated_user')
needs_collection= MongoDB.get_collection('needs')

def create_action(action: Action) -> str:
    if not all([action.created_by,
                action.type]):
        raise ValueError("All fields are mandatory for creation.")
    if (action.type== ActionType.need_resource):
        if action.resources == [] or action.needs == []: #TODO len check
            raise ValueError("At least one need and resource is needed")
        insert_result = actions_collection.insert_one(action.dict())

        if insert_result.inserted_id:
            for need_id in action.needs:
                need = needs_collection.find_one({"_id": ObjectId(need_id)})
                need["status"] = statusEnum.inprogress
                need["action_list"].append(insert_result.inserted_id)
                needs_collection.update_one({"_id": ObjectId(need_id)}, {"$set":need})
            for resource_id in action.resources:
                resource = resources_collection.find_one({"_id": ObjectId(resource_id)})
                resource["status"] = statusEnum.inprogress
                resource["action_list"].append(insert_result.inserted_id)
                resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set":resource})
            
            return ActionSuccess(action_id= str(insert_result.inserted_id))
        else:
            raise ValueError("Action could not be created")
    elif(action.type== ActionType.transport):
        if action.resources == []:
            raise ValueError("At least one resource is needed")
        if not all([action.end_x, action.end_y]):
            raise ValueError("Please enter locations")
        insert_result = actions_collection.insert_one(action.dict())
        if insert_result.inserted_id:
            for resource_id in action.resources:
                resource = resources_collection.find_one({"_id": ObjectId(resource_id)})
                resource["status"] = statusEnum.inprogress
                resource["action_list"].append(insert_result.inserted_id)
                resources_collection.update_one({"_id": ObjectId(resource_id)}, {"$set":resource})
            return ActionSuccess(action_id= str(insert_result.inserted_id))
    elif(action.type== ActionType.move_survivors):
        if action.number_of_people==0:
            raise ValueError("Please enter the number of people that are moved")
        insert_result = actions_collection.insert_one(action.dict())
        return ActionSuccess(action_id= str(insert_result.inserted_id))
        

def find_many():
    actions = actions_collection.find()
    return create_json_for_successful_data_fetch(actions, "actions")

def get_related_needs(resource_id:str):
    resource = resources_collection.find_one({"_id": ObjectId(resource_id)})
    if not resource:
        raise ValueError("There is no resource with this id") 
    need_type = resource['type']
    query = {
    'type': need_type,
    'active': True,
    '$or': [
        {'occur_at':{'$lte': resource['occur_at'] + timedelta(days=3)}},
        {'occur_at':{'$gte': resource['occur_at'] - timedelta(days=3)}}]
    }

    needs = needs_collection.find(query)
    return create_json_for_successful_data_fetch(needs, "needs")

def get_related_resources(need_id:str):
    need = needs_collection.find_one({"_id": ObjectId(need_id)})
    if not need:
        raise ValueError("There is no need with this id") 
    resource_type = need['type']
    query = {
    'type': resource_type,
    'active': True,
    '$or': [
        {'occur_at':{'$lte': need['occur_at'] + timedelta(days=3)}},
        {'occur_at':{'$gte': need['occur_at'] - timedelta(days=3)}}]
    }

    resources = resources_collection.find(query)
    return create_json_for_successful_data_fetch(resources, "resources")

def update(action_id: str, current_user, body):
   
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if not action:
        raise ValueError("There is no action with this id") 
    if action['created_by'] != current_user:
        raise ValueError("Forbidden") 
    updated = actions_collection.update_one({"_id": ObjectId(action_id)},{ '$set':body.dict(exclude_none=True)})
    res = actions_collection.find_one({"_id": ObjectId(action_id)})
    return  res


def get_match_list(action_id:str, current_user:str): #needs [{}] resource
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if not action:
        raise ValueError("There is no action with this id")
    need_match_list = []
    resource_match_list = []

    for need_id in action['needs']:
        need = needs_collection.find_one({"_id": ObjectId(need_id)})
        if not need:
            continue
        if need['status'] == statusEnum.done:
            continue
    
        need_match_list.append(need)
    for resource_id in action['resources']:
        resource = resources_collection.find_one({"_id": ObjectId(resource_id)})
        if not resource:
            continue
        if resource['status'] == statusEnum.done:
            continue
        resource_match_list.append(resource)
    for payload in need_match_list:
        print(payload)
        payload['_id'] = str(payload['_id']) 
        if len(payload['action_list']) != 0 :
            payload['action_list'] = [str(action) for action in payload['action_list']]
    for payload in resource_match_list:
        print(payload)
        payload['_id'] = str(payload['_id']) 
        if len(payload['action_list']) != 0 :
            payload['action_list'] = [str(action) for action in payload['action_list']]
    
    return {"needs": need_match_list, "resources": resource_match_list}



# bir resource needleri karsiliyor
def do_action(action_id:str, current_user:str, match_list):
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if not action:
        raise ValueError("There is no action with this id")
    print(match_list.matches)
    if(action['created_by']!= current_user ):
        raise ValueError("Only user created the action can do it")
    if (action.get("type")== ActionType.need_resource):
        for match in match_list.matches:
            need = needs_collection.find_one({"_id": ObjectId(match.need_id)})
            remain = need['unsuppliedQuantity'] - match.amount
            if(remain<=0):
                need['status'] = 'done'
            else:
                need['unsuppliedQuantity'] = remain
                need['status'] = 'active'
            needs_collection.update_one({"_id": ObjectId(match.need_id)}, {"$set":need})
            resource = resources_collection.find_one({"_id": ObjectId(match.resource_id)})
            remain = resource['currentQuantity'] - match.amount
            if(remain<=0):
                resource['status'] = 'done'
            else:
                resource['currentQuantity'] = remain
                resource['status'] = 'active'
            resources_collection.update_one({"_id": ObjectId(match.resource_id)}, {"$set":resource})
        return {"met_needs": [match.need_id for match in match_list.matches], "used_resources": [match.resource_id for match in match_list.matches]}
    elif(action.get("type")==ActionType.transport):
        for resource_id in action.get('resources'):
            resource = resources_collection.find_one({"_id": ObjectId(resource_id)})
            new_x = action.get("end_x")
            new_y = action.get("end_y")
            update_data = {
                "$set": {
                    "x": new_x,
                    "y": new_y,
                    "status":'active'
                }
            }
            resources_collection.update_one({"_id": ObjectId(resource_id)}, update_data)
        return {"used_resources": [resource_id for resource_id in action.get('resources')]}
    

    action['status'] = "done"
    # update action
    actions_collection.update_one({"_id": ObjectId(action_id)}, {"$set":action})

def get_action(action_id:str) :
    if (ObjectId.is_valid(action_id)):
        query = {"$match":{"_id": ObjectId(action_id)}}
    else:
        raise ValueError(f"Action id {action_id} is invalid")
    projection = {"$project":{"_id": {"$toString": "$_id"},
                  "created_by": 1,
                  "description": 1,
                  "type": 1,
                  "start_x": 1,
                  "start_y": 1,
                  "end_x": 1,
                  "end_y": 1,
                  "number_of_people":1,
                  "status": 1,
                  "x":1,
                  "y":1,
                  "occur_at": 1,
                  "created_at":1,
                  "last_updated_at":1,
                  "upvote":1,
                  "downvote":1,
                  "needs":1,
                  "resources":1,
                  "end_at":1
                  }}
    aggregation = [
        {"$match": {"_id": ObjectId(action_id)} },
        { "$lookup": { "from": "needs", "localField": "needs", "foreignField": "_id","as": "needs_object"}},
        { "$lookup": { "from": "resources", "localField": "resources", "foreignField": "_id","as": "resources_object"}},
        projection
        ]
    actions_data = actions_collection.aggregate(aggregation)
    result_list = create_json_for_successful_data_fetch(actions_data, "action")
    return result_list

# def update_action(action_id: str, action: Action) -> Action:
#     existing_action = actions_collection.find_one({"_id": ObjectId(action_id)})
#     if existing_action:
#         update_data = {k: v for k, v in action.dict(exclude_none=True).items()}

#         if 'created_at' in existing_action:
#             update_data['created_at'] = existing_action['created_at']

#         # Set 'last_updated_at' to the current time
#         update_data['updated_at'] = datetime.now() + timedelta(hours=3)

#         actions_collection.update_one({"_id": ObjectId(action_id)}, {"$set": update_data})

#         updated_action_data = actions_collection.find_one({"_id": ObjectId(action_id)})
#         return Action(**updated_action_data)
#     else:
#         raise ValueError(f"Action id {action_id} not found")
    

def cancel_action(action_id: str, current_user: str):
    
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    #TODO i am checking if the action is already started, but i am not sure if this is the correct way
    if(action.start_at < datetime.datetime.now()):
        raise ValueError('Action already started: {action_id}')
    if action:
        action= Action(**action) 
    else:
        raise ValueError('There is no action with this id: {action_id}')
    if action.created_by!= current_user:
        raise ValueError('Only user that created the action can cancel it')
    
    for need in action.needs:
        restore_need(need)  
    for resource in action.resources:
        restore_resources(resource)
    update_result = actions_collection.update_one(
        {"_id": ObjectId(action_id)},
        {"$set": {"status": statusEnum.inactive}}
        )
   
    return "{\"actions\":[{\"_id\":" + f"\"{action_id}\"" + "}]}"

def delete_action(action_id, current_user:str):
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if action:
        action= Action(**action) 
    else:
        raise ValueError('There is no action with this id: {action_id}')
    if action.created_by!= current_user:
        raise ValueError('Only user that created the action can cancel it')
    if action.status== statusEnum.active:
        raise ValueError('Cant delete an active action, cancel it first')
    d = actions_collection.delete_one({"_id": ObjectId(action_id)})
    if d.deleted_count == 0:
        raise ValueError('Could not delete action')
    return "{\"actions\":[{\"_id\":" + f"\"{action_id}\"" + "}]}"
    

def restore_need(need_id, amount):
    query = {"_id": ObjectId(need_id)}
    need= needs_collection.find_one(query)
    update_result = needs_collection.update_one(
    {"_id": need},
    {"$set": {"active": True }}
    )


def restore_resources(resource_id, action_id):
    query = {"_id": ObjectId(resource_id)}
    resource= resources_collection.find_one(query)
    ah_list= resource['actions_used']
    quantity_found = next((ah_list.get('quantity', 0) for ah_list in resource['actions_used'] if ah_list.get('action_id') == action_id), 0)
    resource['actions_used'] = [ah_list for ah_list in resource['actions_used'] if ah_list.get('action_id') != action_id]
    update_result = resources_collection.update_one(
    {"_id": resource['_id']},
    {"$set": {"active": True, "actions_used": resource['actions_used']}}
    )
    if update_result.modified_count <= 0:
        raise ValueError(f"No ActionHistory found with action_id '{action_id}'.")
    return


                
