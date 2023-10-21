from passlib.apps import postgres_context
from Database.mongo import MongoDB
from datetime import *
import pymongo

db = MongoDB.getInstance()
session_alive_minutes = 720 #half a day

def define_user_password_hash(username:str, password:str):
    hash = postgres_context.hash(password, user=username)
    return hash

def delete_user(username):
    query = {"username": username}
    user_session_file = db.get_collection("user_sessions")
    update_result = user_session_file.delete_many(query)

    # if (update_result.deleted_count > 0):
    #TODO A user with an open session deleted. Decisions?



    query = {"username": username}
    user_file = db.get_collection("users")
    update_result = user_file.delete_one(query)

    return (update_result.deleted_count>0)

def update_user_password(username, password):
    hash = define_user_password_hash(username,password)
    query = {"username": username}
    update_password = {"$set": {"password": hash}}

    user_file = db.get_collection("users")

    update_result = user_file.update_one(query, update_password)
    return update_result.upserted_id

def create_user_with_password(username, password):
    hash = define_user_password_hash(username,password)

    user = {
        "username": username,
        "password": hash
    }

    user_file = db.get_collection("users")

    user_id = user_file.insert_one(user)

    return user_id

def verify_user_password(username:str, password_plain:str):
    query = {"username": username}
    user_file = db.get_collection("users")
    user = user_file.find_one(query)
    if (user is not None):
        return postgres_context.verify(password_plain, user["password"], user=username)

def verify_user_session(username:str, token:str):
    query = {"username": username, "token": token}
    user_session_file = db.get_collection("user_sessions")
    user_cursor = user_session_file.find(query)
    user_session = user_cursor.next()
    if (user_session):
        session_date = user_session["start_date"]
        current_time = datetime.now()
        delta = current_time - session_date
        if (delta.seconds < session_alive_minutes*60):
            return True

    return False

