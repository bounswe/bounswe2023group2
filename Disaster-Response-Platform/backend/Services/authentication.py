from passlib.apps import postgres_context
from Database.mongo import MongoDB
import pymongo

db = MongoDB.getInstance()

def define_user_password_hash(username:str, password:str):
    hash = postgres_context.hash(password, user=username)
    return hash

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
    user_cursor = user_file.find(query)
    user = user_cursor.next()
    return postgres_context.verify(password_plain, user["password"], user=username)
