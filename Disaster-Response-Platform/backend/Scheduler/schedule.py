
from Models.resource_model import ConditionEnum
from Models.action_model_v2 import *
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from bson import json_util
from datetime import datetime, timezone, timedelta
from Services.build_API_returns import *
import Services.resource_service as resource_service
import Services.need_service as need_service
from Models.recurrence_model import RecurringItem
from Validations.recurrence_validation import Recurrence_validation
from apscheduler.schedulers.background import BackgroundScheduler
import datetime
import os
import Scheduler.schedule
from datetime import date

recurrences_collection = MongoDB.get_collection('reccurrences')
needs_collection = MongoDB.get_collection('needs')
resources_collection = MongoDB.get_collection('resources')
scheduler = BackgroundScheduler()

def start():
    scheduler.start()

def tick():
    print('Tick! The time is: %s' % datetime.datetime.now())


print('Press Ctrl+{0} to exit'.format('Break' if os.name == 'nt' else 'C'))
def schedule_recurrence():
    recurrences = recurrences_collection.find({ "status": {"$in":['Continuing', 'Created']}})
    print('recurrence')
    for recur in recurrences:
        add_job(recur)

# Turns a dictionary into a class 
class Dict2Class(object): 
      
    def __init__(self, my_dict): 
          
        for key in my_dict: 
            setattr(self, key, my_dict[key]) 
    def dict(self):
        return self.__dict__
        
      
#TODO add _ids of activities to the recurrences
def add_need(need: Need):
    occur = datetime.datetime.today().strftime('%Y-%m-%d') 
    need['occur_at'] = occur
    need['created_at'] = occur
    result = need_service.create_need(Dict2Class(need))
    print(result)

def add_resource(resource):
    occur = datetime.datetime.today().strftime('%Y-%m-%d') 
    resource['created_at'] = occur
    resource['occur_at'] = occur
    result = resource_service.create_resource(resource)
   

def add_job(job):
    try:
        if(len(job['recurring_items']) == 0):
            return
        if job['end_at'] <= datetime.datetime.now(): 
            recurrences_collection.find_one_and_update({'_id': ObjectId(job['_id'])}, {'$set':{'status': statusEnum.done}})
            delete_job(str(job['_id']))
            return 
        if(job['activity'] == RecurringItem.need):
            first_need_id = job['recurring_items'][0]
            need = needs_collection.find_one({'_id':ObjectId(first_need_id)})
            exclude_fields = ['_id', 'occur_at', 'active', 'occur_at', 'upvote', 'downvote', 'created_at', 'last_updated_at','action_list', 'status']
            need_new = {key: value for key, value in need.items() if key not in exclude_fields}
            need_new['recurrence']= str (job['_id'])
            occurrance = {job['occurance_unit']+ 's': 1/job['occurance_rate']}
            occurrance['id'] = str(job['_id'])
            scheduler.add_job(add_need, 'interval',args =[need_new], **occurrance)
        if(job['activity'] == RecurringItem.resource):
            first_resource_id = job['recurring_items'][0]
            resource = resources_collection.find_one({'_id':ObjectId(first_resource_id)})
            exclude_fields = ['_id', 'occur_at','currrenQuantity', 'active', 'occur_at', 'upvote', 'downvote', 'created_at', 'last_updated_at','action_list', 'status', 'actions_used']
            resource_new = {key: value for key, value in resource.items() if key not in exclude_fields}
            resource_new['recurrence']= str (job['_id'])
            occurrance = {job['occurance_unit']+ 's': 1/job['occurance_rate']}
            occurrance['id'] = str(job['_id'])
            scheduler.add_job(add_resource, 'interval',args =[resource_new], **occurrance)
    except Exception as e:
        print(e)
        return
       
    
def cancel_job(_id):
    try:
        scheduler.pause_job(job_id = str(_id))
    except:
        print(f'ERROR_CANCEL_JOB {str(_id)}')
        raise ValueError('ERROR_CANCEL_JOB')

def resume_job(_id):
    try:
        resumed = scheduler.resume_job(job_id = str(_id))
    except:
        print(f'ERROR_RESUME_JOB {str(_id)}')
        raise ValueError()

def delete_job(_id):
    try:
        deleted = scheduler.remove_job(job_id = str(_id))
        print(deleted)
    except:
        print(f'ERROR_DELETE_JOB {str(_id)}')
        raise ValueError()
