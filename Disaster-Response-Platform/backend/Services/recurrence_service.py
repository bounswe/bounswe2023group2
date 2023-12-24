from Models.resource_model import ConditionEnum
from Models.action_model_v2 import *
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from bson import json_util
from datetime import datetime, timezone, timedelta
from Services.build_API_returns import *
from Services.resource_service import *
from Services.need_service import *
from Models.recurrence_model import *
from Validations.recurrence_validation import SuccessResponse, FindOneResponse
import Scheduler.schedule as schedule
# Get the resources collection using the MongoDB class
actions_collection = MongoDB.get_collection('actions')
resources_collection= MongoDB.get_collection('resources')
users_collection= MongoDB.get_collection('authenticated_user')
needs_collection= MongoDB.get_collection('needs')
events_collection= MongoDB.get_collection('events')
recurrences_collection = MongoDB.get_collection('reccurrences')

def create(recurrence: Recurrence, current_user) -> str:
    if not all([recurrence.title, recurrence.occurance_rate,recurrence.occurance_unit, recurrence.activity, recurrence.start_at, recurrence.end_at]):
        raise ValueError("All fields are mandatory for creation.")
    try:
        insert = recurrence.dict()
        insert['created_by'] = current_user
        insert = recurrences_collection.insert_one( recurrence.dict())
        result = recurrences_collection.find_one({'_id':ObjectId(insert.inserted_id)})

        return SuccessResponse(recurrence_id= str(insert.inserted_id),message='CREATE_RECURRENCE_SUCCESS')
    except:
        raise ValueError("Recurrence could not be created")

def attach_activity(attach, user):
    if not all([attach.recurrence_id, attach.activity_type, attach.activity_id]):
        raise ValueError("All fields are mandatory for creation.")
    try:
        insert = recurrences_collection.find_one({'_id':ObjectId(attach.recurrence_id)})
        # if(insert['created_by']!= user):
        #     raise ValueError("FORBIDDEN")
        items = insert['recurring_items']
        activity = {}
        if(attach.activity_type=='need'):
            activity = needs_collection.find_one({'_id':ObjectId(attach.activity_id)})
        if(attach.activity_type=='resource'):
            activity = resources_collection.find_one({'_id':ObjectId(attach.activity_id)})
        if(attach.activity_type=='event'):
            activity = events_collection.find_one({'_id':ObjectId(attach.activity_id)})
        # if(activity['occur_at'] < insert['start_at']):
        #     raise ValueError("Activity cannot be attached")
        items.append(str(activity['_id']))
        recurrences_collection.update_one({'_id':ObjectId(attach.recurrence_id)},{'$set':{'recurring_items':items}})
        return SuccessResponse(recurrence_id= str(insert._id), message='ATTACH_RECURRENCE_SUCCESS')
    except:
        raise ValueError("RECURRENCE_ATTACH_ERROR")

def start_recurrence(_id):
    recurrence = recurrences_collection.find_one({'_id':ObjectId(_id)})

    if(len(recurrence['recurring_items'])<=0):
        raise ValueError('item not found')
    schedule.add_job(recurrence)
    return SuccessResponse(recurrence_id= str(_id), message='START_RECURRENCE_SUCCESS')

def find_one(_id):
    try:
        payload = recurrences_collection.aggregate([
            {'$match':{'_id':ObjectId(_id)}},
            { "$lookup": {
                "from": "$books",
                "foreignField": "_id",
                "localField": "books",
                "as": "books"
            }}
        ])
        return FindOneResponse(payload,  message='ATTACH_RECURRENCE_SUCCESS')
    except:
        raise ValueError("RECURRENCE_ATTACH_ERROR")

def find_many():
    try:
        payload = recurrences_collection.find()
        result_list = create_json_for_successful_data_fetch(payload, "recurrences")
        return result_list
    except:
        raise ValueError('RECURRENCE_FIND_MANY_ERROR')

def cancel(_id, current_user):
    try:
        updated = recurrences_collection.find_one_and_update({'_id':ObjectId(_id)}, {'$set': {'status':statusEnum.cancelled}})
        schedule.cancel_job(_id)
        return SuccessResponse(recurrence_id=_id, message='Success')
    except:
        raise ValueError('RECURRENCE_CANCEL_ERROR')
    
def delete(_id, current_user):
    try:
        updated = recurrences_collection.find_one_and_delete({'_id':ObjectId(_id)})
        schedule.delete_job(_id)
        return SuccessResponse(recurrence_id=_id, message='Success')
    except:
        raise ValueError('RECURRENCE_DELETE_ERROR')

def resume(_id, current_user):
    try:
        recurrence = recurrences_collection.find_one({'_id':ObjectId(_id)})
        if recurrence['end_date'] < datetime.datetime.now(): 
            raise ValueError('RECURRENCE_FINISHED_BEFORE')
        updated = recurrences_collection.find_one_and_update({'_id':ObjectId(_id)}, {'$set': {'status':statusEnum.inprogress}})
        schedule.resume_job(_id)
        return SuccessResponse(recurrence_id=_id, message='Success')
    except:
        raise ValueError('RECURRENCE_RESUME_ERROR')

