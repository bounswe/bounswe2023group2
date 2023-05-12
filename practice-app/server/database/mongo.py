from pymongo import MongoClient
import urllib 
from config import Config

def get_authentication_for_db() -> str:
    username = Config.MONGO_USERNAME
    password = Config.MONGO_PASSWORD

    return username, password


class MongoDB(object):
    __instance = None

    @staticmethod
    def getInstance(address="localhost", port=27017, username=None, password=None):
        """ Static access method. """
        if MongoDB.__instance == None:
            if username is None or password is None:
                username, password = get_authentication_for_db()
            db_url = f"mongodb://{address}:{port}/"
            MongoDB.__instance = MongoDB(db_url)
        return MongoDB.__instance

    @staticmethod
    def get_collection(collection_name:str):
        if MongoDB.__instance == None:
            MongoDB.getInstance()
        
        return MongoDB.__instance.db[collection_name]
        

    def __init__(self, mongoUrl="mongodb://mongo:27017/"):
        self.db = MongoClient(mongoUrl).practiceApp
    
