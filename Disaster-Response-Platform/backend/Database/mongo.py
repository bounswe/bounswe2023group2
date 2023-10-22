from pymongo import MongoClient
from passlib.apps import postgres_context

DARP_DATABASE_NAME = "DARP"

class MongoDB(object):
    __instance = None

    @staticmethod
    def getInstance(address="localhost", port=27017, username=None, password=None):
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

    def initialize_collections(self):
        user_collection = self.db.create_collection("users")
        sesion_collection = self.db.create_collection("user_sessions")

        user = {
            "username": "mongostarter",
            "password": "mongoIsNotMongol"
        }

        hash = postgres_context.hash( user["password"], user = user["username"])
        user["password"] = hash

        user_file = self.db.get_collection("users")

        user_id = user_file.insert_one(user)

    def __init__(self, mongoUrl="mongodb://mongo:27017/"):
        client = MongoClient(mongoUrl)
        db_list = client.list_database_names()
        self.db = client[DARP_DATABASE_NAME]
        if (DARP_DATABASE_NAME not in db_list):
            self.initialize_collections()

