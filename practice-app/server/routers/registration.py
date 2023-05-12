from fastapi import APIRouter, HTTPException
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
from database.registeredUser import *
import os
from twilio.rest import Client
from config import Config
import requests


account_sid = Config.ACCOUNT_SID
auth_token = Config.AUTH_TOKEN
#client = Client(account_sid, auth_token) #this is where I get twilio api

router = APIRouter()

db = MongoDB.getInstance()

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
    url = f'https://api.twilio.com/2010-04-01/Accounts/{account_sid}/Messages.json'
    currentdata = {
        'From': '+13184952743',
        'Body': 'Welcome to our app, you are registered successfully! Please log in to our system. We will develop more in CMPE451. Wait for it!!',
        'To': '+9'+currentUser.phone_number,
    }
    response = requests.post(url, data=currentdata, auth=(account_sid, auth_token))
    print(response.status_code)
    print(response.json())
    return {"is_exist":False,"pw_not_ok":False,"phone_not_turkey":False,"registered":True} #when they are set as True, in front end it shows me the warnings as a default so I negate them.
    