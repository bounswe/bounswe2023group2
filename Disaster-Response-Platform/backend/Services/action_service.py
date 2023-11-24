from Models.resource_model import ConditionEnum
from Models.action_model import *
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from datetime import datetime, timezone, timedelta
from Services.build_API_returns import *
from Services.resource_service import *
from Services.need_service import *

# Get the resources collection using the MongoDB class
actions_collection = MongoDB.get_collection('actions')
resources_collection= MongoDB.get_collection('resources')

needs_collection= MongoDB.get_collection('needs')
def create_action(action: Action) -> str:
    # Manual validation for required fields during creation
    #can there be actions without a resource

    ## todo burayı düzelt koçum
    ## eklerken type ı need_resoruce match ise , related needs ve related resources empty mi diye check ederim
    # 
    if not all([action.created_by,
                action.type]):
        raise ValueError("All fields are mandatory for creation.")
    if (action.type== ActionType.need_resource):
        if(not all([action.endLocation_x,action.start_location_x, action.start_location_y, action.endLocation_y])):
            raise ValueError("All fields are mandatory for creation.")
        #action ın need ve resource larına bakalım while içinde listin tüm elemanlarını tarverse edip need ve resource un recur untilinden uzun olamasın
        # burda recurrence ı anlayınca tüm seriyi listeye ekleyeyim en iyisi
        # buraya need ve resource type , detail check koyalım aynı type ta değilse hata versin
        i=0
        for group in action.related_groups:

            min_datetime = datetime.now(timezone.utc)
        

            #to check resource type and need type match
            id= group.related_resources[0]
            resource=resources_collection.find_one({"_id": ObjectId(id)})
            print(resource)
            type= resource["type"]
            id_list=[]
            for id in group.related_resources:
                print(id)
                related_resource = resources_collection.find_one({"_id": ObjectId(id)})
                if related_resource["type"] != type:
                    raise ValueError("You can only take action between resource and needs that has the same type")
      
                print("debug2",related_resource["created_by"])
                print("groups",action.related_groups)
                if(related_resource.get("recurrence_id") is not None):
                    #find all recurrences with r_id 
                    print("resource recurrence id is not none")
                    query = {"recurrence_id": related_resource["recurrence_id"]}
                    matching_resources = resources_collection.find(query, {"_id": 1})
                    rid_list = [str(resource["_id"]) for resource in matching_resources]
                    id_list.extend(rid_list)
                  
                    if(min_datetime> related_resource["recurrence_deadline"]):
                        min_datetime=related_resource["recurrence_deadline"]
            #add recuuring resource list to action's mentioned resource list
            action.related_groups[i].related_resources.extend(id_list)
            for id in group.related_needs:
                print(id)
                print("groups",action.related_groups)
                if related_resource["type"] != type:
                    raise ValueError("You can only take action between resource and needs that has the same type")
                related_need= needs_collection.find_one({"_id": ObjectId(id)})
                if(related_need.get("recurrence_id") is not None):
                    print("need recurrence id is not none")
                    print(related_need.get("recurrence_id"))
                    print("end date of action",action.end_at)
                    print("min date_Time",min_datetime)
                    #find all recurrences with r_id 
                    query = {"recurrence_id": related_need["recurrence_id"]}
                    matching_needs = needs_collection.find(query, {"_id": 1})
                    nid_list = [str(need["_id"]) for need in matching_needs]
                    id_list.extend(nid_list)
                    #add recuuring resource list to action's mentioned resource list
                    
                    #TODO burda date leri kıyaslayamıyom kafayı yiycem
                    # if(min_datetime> related_need["recurrence_deadline"]):
                    #     min_datetime=related_need["recurrence_deadline"]
                    
            action.related_groups[i].related_needs.extend(id_list)
            i+=1
        print("end date of action",action.end_at)
        if(action.end_at is not None):
            if(action.end_at > min_datetime):
                raise ValueError("Action end time should be set properly")
        
        insert_result = actions_collection.insert_one(action.dict())
   
    #check the result to change from
    if insert_result.inserted_id:
        return ActionSuccess(action_id= str(insert_result.inserted_id))
    else:
        raise ValueError("Action could not be created")

def do_action(action_id:str, current_user:str):
    action = actions_collection.find_one({"_id": ObjectId(action_id)})
    if(action.created_by!= current_user ):
        raise ValueError("Only user created the action can do it")
    #burda da aynı date teki aynı tipteki resource ve needleri birleştireyim (RESOURCE u eksiltelim, need i karşılayalım)
    # resource need den az ise: 
    # TODO (aynı date te birden fazla resource caseini inceler)
    

    

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
                  "related_needs":1,
                  "related_resources":1,
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
    

def delete_action(action_id: str):
    try:
        d = actions_collection.delete_one({"_id": ObjectId(action_id)})
        if d.deleted_count == 0:
            raise
        return "{\"actions\":[{\"_id\":" + f"\"{action_id}\"" + "}]}"
        # Returning the deleted id would be nice
    except:
        raise ValueError(f"Actions {action_id} cannot be deleted")
