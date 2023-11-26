from Models.resource_model import ConditionEnum
from Models.action_model import *
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
    # Manual validation for required fields during creation
    #can there be actions without a resource

    if not all([action.created_by,
                action.type]):
        raise ValueError("All fields are mandatory for creation.")
    if (action.type== ActionType.need_resource):
        if not action.related_groups:
            raise ValueError("All fields are mandatory for creation.")
        #if(not all([action.endLocation_x,action.start_location_x, action.start_location_y, action.endLocation_y])):
        #    raise ValueError("All fields are mandatory for creation.")
        i=0
        for group in action.related_groups:
            if not group.related_resources:
                raise ValueError("Please enter resource id")
            def normalize_date(dt):
                return datetime(dt.year, dt.month, dt.day)
            id= group.related_resources[0]
            resource=resources_collection.find_one({"_id": ObjectId(id)})
            if not resource:
                raise ValueError(f"This is not a valis resource id {id}")
            #TODO resource need active mi check i ekle
            type= resource["type"]

            id_list=[]
            for id in group.related_resources:
                related_resource = resources_collection.find_one({"_id": ObjectId(id)})
                if related_resource["type"] != type:
                    raise ValueError("You can only take action between resource and needs that has the same type")
                if related_resource["active"]==False:
                    raise ValueError("You have entered a inactive resource id")

                if(related_resource.get("recurrence_id") is not None):
                    #find all recurrences with r_id 
                    id_list.extend(get_resource_list_by_id(id))
                    action.related_groups[i].recurrence=True
                    if( normalize_date(related_resource["recurrence_deadline"]) < normalize_date(action.end_at)):
                        raise ValueError("Action end date cant be longer than the resource recurrence deadline")
                else:
                    id_list.append(id)
                    if( not (normalize_date(related_resource["occur_at"]) <= normalize_date(action.end_at)) and (normalize_date(related_resource["occur_at"])>= normalize_date(action.occur_at)) ):
                        #print(normalize_date(related_resource["occur_at"]),normalize_date(action.end_at), normalize_date(related_resource["occur_at"]),normalize_date(action.occur_at)  )
                        raise ValueError("Action and resource should occur at the same date")
            #add recuuring resource list to action's mentioned resource list
            action.related_groups[i].related_resources= id_list
            id_list=[]
            for id in group.related_needs:
                if not group.related_needs:
                    raise ValueError("Please enter need ids")
                related_need= needs_collection.find_one({"_id": ObjectId(id)})
                if not related_need:
                    raise ValueError("Not a valid need id")
                if related_need["type"] != type:
                    raise ValueError("You can only take action between resource and needs that has the same type")
                
                if related_need["active"]==False:
                    raise ValueError("You have entered a inactive need id")
                
                if(related_need.get("recurrence_id") is not None):
                    id_list.extend(get_need_list_by_id(id))
                    action.related_groups[i].recurrence=True
                    if(normalize_date(related_need["recurrence_deadline"]) < normalize_date(action.end_at)):
                        raise ValueError("Action end date cant be longer than the need recurrence deadline")
                else:
                    id_list.append(id)
                    if( not(normalize_date(related_need["occur_at"]) <= normalize_date(action.end_at) and normalize_date(related_need["occur_at"])>= normalize_date(action.occur_at)) ):
                        raise ValueError("Action and need should occur at the same date")     
            action.related_groups[i].related_needs=(id_list)
            action.related_groups[i].group_type=type
            
            i+=1
        action.status= statusEnum.created
        insert_result = actions_collection.insert_one(action.dict())
   
        #check the result to change from
        if insert_result.inserted_id:
            return ActionSuccess(action_id= str(insert_result.inserted_id))
        else:
            raise ValueError("Action could not be created")
    

def do_action(action_id:str, current_user:str):
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if not action:
        raise ValueError("There is no action with this id")
    
    if(action['created_by']!= current_user ):
        raise ValueError("Only user created the action can do it")
    groups= action["related_groups"]
    met_needs=[]
    for group in groups:
        
        related_needs= group['related_needs']
        related_resources= group['related_resources']
        #get need objects
        current_date = action['occur_at']
        totalQuantityMet=0

        related_needs_object_ids = [ObjectId(id_str) for id_str in related_needs]
        related_resources_object_ids = [ObjectId(id_str) for id_str in related_resources]
        checkTotalQuantity(related_needs,related_resources, current_date, group["group_type"])
        #for each action day query for that days need and resources
        if group["recurrence"]==True:
            while(current_date <= action['end_at']):
                # Query to find documents with matching IDs and occur_at time
                end_date= current_date + timedelta(days=1)
                query = {
                "_id": {"$in": related_needs_object_ids},
                'occur_at': current_date,
                'active':True
                }
                needs= needs_collection.find(query)
                needs= list(needs)
                
                #get resource objects that occurs at date of action
                query = {
                    "_id": {"$in": related_resources_object_ids},
                    'occur_at': {'$gte': current_date, '$lt': end_date},
                    'active': True
                }
                resources= resources_collection.find(query)
                resources= list(resources)
                print(current_date)
                totalQuantityMet+= update_need_resources(resources,needs, action_id)
                current_date += timedelta(days=1) #this can be optimized to min recurrence rate
        else:
            query = {"_id": {"$in": related_needs_object_ids}}
            needs= needs_collection.find(query)
            needs= list(needs)

            query = {"_id": {"$in": related_resources_object_ids}}
            resources= resources_collection.find(query)
            resources= list(resources)
            totalQuantityMet+= update_need_resources(resources,needs, action_id)
            

        if(totalQuantityMet==0):
            raise ValueError("There is either no need for this resource or no resource for this need")
        met_needs.append(totalQuantityMet)
    update_result = actions_collection.update_one(
        {"_id": action['_id']},
        {"$set": {"status": statusEnum.active}}
        )
    ret= doActionResponse(met_needs=met_needs)
    return ret
def get_action(action_id:str):
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if action:
        return Action(**action)   
    else:
        raise ValueError("Action not found")
def update_need_resources(resources,needs, action_id):
    r=0
    i=0
    totalQuantityMet=0
    if len(resources)!=0 and len(needs)!=0:
                
        while(i<len(needs) and r< len(resources)):
            resource= resources[r]
            need= needs[i]
            if need['unsuppliedQuantity']<resource['currentQuantity']:
                left= resource['currentQuantity']- need['unsuppliedQuantity']
                totalQuantityMet+= need['unsuppliedQuantity']
                update_need(need, False, left,need['unsuppliedQuantity'])
                
                i+=1
                
            elif need['unsuppliedQuantity']>resource['currentQuantity']:
                left= need['unsuppliedQuantity']-resource['currentQuantity']
                update_resource(resource, False, left,resource['currentQuantity'],action_id)
                r+=1
            else:
                left=0
                totalQuantityMet+= need['unsuppliedQuantity']
                update_need(need, False, left, need['unsuppliedQuantity'])
                update_resource(resource, False,left,resource['currentQuantity'], action_id)
                i+=1
                r+=1
        if len(needs)<= i:
            i-=1
        if len(resources)<=r:
            r-=1
        if resources[r]['active']==False:
            totalQuantityMet+= need['unsuppliedQuantity']
            update_need(need, True, left, need['unsuppliedQuantity']-left)
        if needs[i]['active']==False:
            update_resource(resource, True, left, resource['currentQuantity']-left, action_id)
    return totalQuantityMet
   
def update_need(need, active, left, action_used):
    need['active']=active
    
    q=left
    if not active:
        q=0
    update_result = needs_collection.update_one(
        {"_id": need['_id']},
        {"$set": {"active": active, "unsuppliedQuantity": q, "action_used": action_used}}
    )
    return
def update_resource(resource, active, left, action_used, action_id):
    resource['active']=active
    print(resource['_id'])
   # Ensure actions_used is initialized as an empty list if it's None
    if resource and resource.get('actions_used') is None:
        
        resource['actions_used'] = []

    # Append the new ActionHistory to the list
    ah= ActionHistory(quantity=action_used, action_id=action_id).dict()
    resource['actions_used'].append(ah)
    q=left
    if not active:
        q=0
    update_result = resources_collection.update_one(
        {"_id": resource['_id']},
        {"$set": {"active": active, "currentQuantity": q, "actions_used": resource['actions_used']}}
    )
    return


    #burda da aynı date teki aynı tipteki resource ve needleri birleştireyim (RESOURCE u eksiltelim, need i karşılayalım)
    # resource need den az ise: 
    # TODO (aynı date te birden fazla resource caseini inceler)
def checkTotalQuantity(related_needs,related_resources, currentdate, type):
    related_needs_object_ids = [ObjectId(id_str) for id_str in related_needs]
    related_resources_object_ids = [ObjectId(id_str) for id_str in related_resources]
    #print("total chck", related_needs)
    query = {
        "_id": {"$in": related_needs_object_ids},
        "occur_at": currentdate
    }

    needs_cursor = needs_collection.find(query)
    needs = list(needs_cursor)

    # MongoDB aggregation pipeline to sum unsuppliedQuantity
    pipeline = [
        {"$match": query},
        {"$group": {
            "_id": None,
            "total_unsupplied_quantity": {"$sum": "$unsuppliedQuantity"}
        }}
    ]
    # Execute the aggregation pipeline
    result = needs_collection.aggregate(pipeline)
    # Extract the result
    total_unsupplied_quantity = next(result, {"total_unsupplied_quantity": 0})["total_unsupplied_quantity"]
   
    query = {
        "_id": {"$in": related_resources_object_ids},
        "occur_at": currentdate
    }
    # MongoDB aggregation pipeline to sum unsuppliedQuantity
    pipeline = [
        {"$match": query},
        {"$group": {
            "_id": None,
            "total_currentQuantity": {"$sum": "$currentQuantity"}
        }}
    ]
    # Execute the aggregation pipeline
    result = resources_collection.aggregate(pipeline)

    # Extract the result
    total_currentQuantity = next(result, {"total_currentQuantity": 0})["total_currentQuantity"]
    if total_unsupplied_quantity> total_currentQuantity:
        raise ValueError(f"There is not enough resource for this {type} need at {currentdate}")
    else:
        return total_unsupplied_quantity
    

def get_group_info(related_group: ActionGroup):
    related_needs= related_group.related_needs
    related_resources= related_group.related_resources
    id= related_resources[0]
    resource=resources_collection.find_one({"_id": ObjectId(id)})
    if not resource:
        raise ValueError(f"Cant found the resource with id: {id}")
    
    type= resource["type"]
    resourceTexts=[]
    needTexts=[]
    for id in related_resources:
        related_resource = resources_collection.find_one({"_id": ObjectId(id)})
        if not related_resource:
            raise ValueError(f"Cant found the reosurce with id: {id}")
        if related_resource['active']==False:
            raise ValueError("You have entered an inactive resource id")
        if related_resource["type"] != type:
            raise ValueError("You can only take action between resource and needs that has the same type")
        if(related_resource.get("recurrence_id") is not None):
            resourceTexts.append(f"From {related_resource['occur_at']}, to {related_resource['recurrence_deadline']} e {type} resource" )
        else:
            resourceTexts.append(f"At {related_resource['occur_at']} {type} resource")
    for id in related_needs:
        related_need = needs_collection.find_one({"_id": ObjectId(id)})
        if not related_need:
            raise ValueError(f"Cant found the need with id: {id}")
        if related_need['active']==False:
            raise ValueError("You have entered an inactive need id")
        if related_need["type"] != type:
            raise ValueError("You can only take action between resource and needs that has the same type")
        if(related_need.get("recurrence_id") is not None):
            needTexts.append(f"From {related_need['occur_at']}, to {related_need['recurrence_deadline']} {type} need for every {related_need['recurrence_rate']} day" )
        else:
            needTexts.append(f"At {related_need['occur_at']} {type} need")
    ret= groupCheckResponse(type=type,resourceTexts=resourceTexts, needTexts=needTexts)
    return ret



def get_resource_list_by_id(resource_id: str):
    resource = resources_collection.find_one({"_id": ObjectId(resource_id)})
    if not resource:
        raise ValueError("This error does not exist")
    if(resource.get("recurrence_id") is not None):
        #find all recurrences with r_id 
        query = {"recurrence_id": resource["recurrence_id"]}
        matching_resources = resources_collection.find(query, {"_id": 1})
        nid_list = [str(resource["_id"]) for resource in matching_resources]
        return nid_list
    else: 
        return resource_id


def get_need_list_by_id(need_id: str):
    need = needs_collection.find_one({"_id": ObjectId(need_id)})
    if not need:
        raise ValueError("This error does not exist")
    if(need.get("recurrence_id") is not None):
        #find all recurrences with r_id 
        query = {"recurrence_id": need["recurrence_id"]}
        matching_needs= needs_collection.find(query, {"_id": 1})
        nid_list = [str(need["_id"]) for need in matching_needs]
        return nid_list
    else: 
        return need_id

def get_action_by_id(action_id: str) -> list[dict]:
    return get_actions(action_id)


def get_actions(action_id:str = None) -> list[dict]:
    projection = {"_id": {"$toString": "$_id"},
                  "created_by": 1,
                  "description": 1,
                  "type": 1,
                  "start_location_x": 1,
                  "start_location_y": 1,
                  "endLocation_x": 1,
                  "endLocation_y": 1,
                  "status": 1,
                  "x":1,
                  "y":1,
                  "occur_at": 1,
                  "created_at":1,
                  "last_updated_at":1,
                  "upvote":1,
                  "downvote":1,
                  "related_groups":1,
                  "end_at":1
                  }
    #projection["_id"] = {"$toString": "$_id"}
    if (action_id is None):
        query = {}
    else:
        if (ObjectId.is_valid(action_id)):
            query = {"_id": ObjectId(action_id)}
        else:
            raise ValueError(f"Action id {action_id} is invalid")

    actions_data = actions_collection.find(query, projection)

    if (actions_data.explain()["executionStats"]["nReturned"] == 0):
        if action_id is None:
            action_id = ""
        raise ValueError(f"Action {action_id} does not exist")
    result_list = create_json_for_successful_data_fetch(actions_data, "actions")
    return result_list


def update_action(action_id: str, action: Action) -> Action:
    # Fetch the existing resource
    existing_action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if existing_action:
        # if 'details' in action.dict(exclude_none=True) and 'details' in existing_action:
        #     action.details = {**existing_action['details'], **resource.dict(exclude_none=True)['details']}

        update_data = {k: v for k, v in action.dict(exclude_none=True).items()}

         # Retain the original 'created_at' field from the existing resource
        if 'created_at' in existing_action:
            update_data['created_at'] = existing_action['created_at']

        # Set 'last_updated_at' to the current time
        update_data['last_updated_at'] = datetime.now()

        actions_collection.update_one({"_id": ObjectId(action_id)}, {"$set": update_data})

        updated_action_data = actions_collection.find_one({"_id": ObjectId(action_id)})
        return Action(**updated_action_data)
    else:
        raise ValueError(f"Action id {action_id} not found")
    

def cancel_action(action_id: str, current_user: str):
    
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if action:
        action= Action(**action) 
    else:
        raise ValueError('There is no action with this id: {action_id}')
    if action.created_by!= current_user:
        raise ValueError('Only user that created the action can cancel it')
    
    for group in action.related_groups:
        restore_needs(group.related_needs)
        restore_resources(group.related_resources, action_id)
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
    

def restore_needs(related_needs: List[str]):
    related_needs_object_ids = [ObjectId(id_str) for id_str in related_needs]
    query = {"_id": {"$in": related_needs_object_ids}}
    needs= needs_collection.find(query)
    needs= list(needs)
    for need in needs:
        if need['active']==True:
            raise ValueError('This action is already cancelled')
        unSuppliedQuantity= need['action_used']
        update_result = needs_collection.update_one(
        {"_id": need['_id']},
        {"$set": {"active": True, "unsuppliedQuantity": unSuppliedQuantity, "action_used": 0}}
        )
    

def restore_resources(related_resources: List[str], action_id):
    related_resources_object_ids = [ObjectId(id_str) for id_str in related_resources]
    query = {"_id": {"$in": related_resources_object_ids}}
    resources= resources_collection.find(query)
    resources= list(resources)
   

    for resource in resources:
        ah_list= resource['actions_used']
        quantity_found = next((ah_list.get('quantity', 0) for ah_list in resource['actions_used'] if ah_list.get('action_id') == action_id), 0)
        resource['actions_used'] = [ah_list for ah_list in resource['actions_used'] if ah_list.get('action_id') != action_id]
        quantity= quantity_found+ resource['currentQuantity']
        update_result = resources_collection.update_one(
        {"_id": resource['_id']},
        {"$set": {"active": True, "currentQuantity": quantity, "actions_used": resource['actions_used']}}
        )
        if update_result.modified_count <= 0:
            raise ValueError(f"No ActionHistory found with action_id '{action_id}'.")
    return
                
