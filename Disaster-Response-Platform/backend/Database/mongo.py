from pymongo import MongoClient
import pymongo
import urllib


class MongoDB(object):
    __instance = None

    @staticmethod
    def getInstance(address="mongo", port=27017, username=None, password=None):
        """ Static access method. """
        if MongoDB.__instance == None:
            db_url = f"mongodb://{address}:{port}/"
            MongoDB.__instance = MongoDB(db_url)
        return MongoDB.__instance

    @staticmethod
    def get_collection(collection_name: str):
        if MongoDB.__instance == None:
            MongoDB.getInstance()

        return MongoDB.__instance.db[collection_name]

    def __init__(self, mongoUrl="mongodb://mongo:27017/"):
        self.db = MongoClient(mongoUrl).darp_db
