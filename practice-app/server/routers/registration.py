from fastapi import APIRouter, HTTPException
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
from database.registeredUser import *
import os
from twilio.rest import Client
from config import Config
import requests

account_sid = "ACd083ee865264ddf205129b6e8eab2b40"
auth_token = "08265669c44acb103273b685a4219d45"
#client = Client(account_sid, auth_token) #this is where I get twilio api

client = Client(account_sid, auth_token)

def validateNumber(phone_number: str): #this method for append numbers to my trial account's verified outgoint caller numbers
    #however because my account is trial, I couldn't add them from there. If it's full account I can do that
    # thus I will add them manually if you want to received an sms from our app when you are registered as a user
    validation_request = client.validation_requests \
        .create(
            friendly_name='Third Party VOIP Number',
            status_callback='https://somefunction.twil.io/caller-id-validation-callback',
            phone_number= phone_number
        )
    print(validation_request.friendly_name)

router = APIRouter()

db = MongoDB.getInstance()

def sendMessage(phone_number: str): #this is a method to send a message to given phone number
    url = f'https://api.twilio.com/2010-04-01/Accounts/{account_sid}/Messages.json' #here is the endpoint
    currentdata = { #here is the data to pass
        'From': '+13184952743',
        'Body': 'Welcome to our app, you are registered successfully! Please log in to our system. We will develop more in CMPE451. Wait for it!!',
        'To': phone_number,
    }
    response = requests.post(url, data=currentdata, auth=(account_sid, auth_token)) #it posts data to api, which sends to SMS
    print(response.status_code)
    print(response.json())

@router.post("/",)
async def register_user(currentUser :RegisteredUser): #it gets user_collection to inserting
    userDb = db.get_collection("authenticated_user") # get the user collection to make insert operation
    if (userDb.find_one({"username":currentUser.username}) !=None) : #if there is a user already existed with current username
        return {"is_exist":True,"pw_not_ok":False,"phone_not_turkey":False,"registered":False} #return is_exist true as a response
    elif (len(currentUser.password) < 8) : #if username is not existed in db but password contains less than 8 characters
        return {"is_exist":False,"pw_not_ok":True,"phone_not_turkey":False,"registered":False} #return is_exist true as a response
    elif (len(currentUser.phone_number)!= 11 or currentUser.phone_number[:2]!= "05"): #if phone number is not valid
        return {"is_exist":False,"pw_not_ok":False,"phone_not_turkey":True,"registered":False}
    #if password has at least 8 characters and username isn't used by another user, insert it to db
    userDb.insert_one({"username":currentUser.username, "first_name": currentUser.first_name, "last_name": currentUser.last_name, "email":currentUser.email, "phone_number":currentUser.phone_number, "password":currentUser.password})
    print("+9"+currentUser.phone_number)
    # I can send sms to the Verified an Outgoing Caller numbers in my trial account
    # validateNumber("+9"+currentUser.phone_number) #Unable to create record: Sorry! Placing verification calls is not supported on trial accounts. Please upgrade to a full account first 
    # thus I need to add the Verify an Outgoing Caller ID on my trial account first. Please contact me to test them! 
    sendMessage("+9"+currentUser.phone_number)
    return {"is_exist":False,"pw_not_ok":False,"phone_not_turkey":False,"registered":True} #when they are set as True, in front end it shows me the warnings as a default so I negate them.
    
