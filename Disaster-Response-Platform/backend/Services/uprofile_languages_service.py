import datetime

from Models.user_profile_model import UserOptionalInfo
from Models.user_profile_model import UserLanguage
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from Services.build_API_returns import *

profile_languages = MongoDB.get_collection('user_languages')

#def set update fields list will be prep

def set_Nones_to_old_values(dict_with_Nones:dict, dict_with_Olds:dict):
    for key, value in dict_with_Nones.items():
        if (value is None):
            dict_with_Nones[key] = dict_with_Olds[key]

def get_user_language(username:str = None, language:str = None) -> str:
    projection = {"_id": 0}

    query = {}


    if (username is not None):
        if language is not None:
            query = {"username": username, "language": language}
        else:
            query = {"username": username}
    else:
        if language is not None:
            query = {"language": language}
        else:
            query = {}

    info_from_db = profile_languages.find(query, projection)
    if (info_from_db.explain()["executionStats"]["nReturned"] == 0):
        raise ValueError("User language does not exist")
    result = create_json_for_successful_data_fetch(info_from_db, "user_languages")
    return result

def add_user_language(user_language:UserLanguage) -> str:
    if not (user_language.username):
        raise ValueError("Username missing")

    if not all([user_language.language,user_language.language_level]):
        raise ValueError("Level or language missing")

    query = {"username": user_language.username, "language": user_language.language}
    info_from_db = profile_languages.find_one(query)

    if (info_from_db is None):
        result = profile_languages.insert_one(dict(user_language))
        if result.inserted_id:
            return json.dumps(dict(user_language))
        else:
            raise ValueError("Unable to insert language")
    else:
        result = profile_languages.update_one(query, {"$set": user_language.dict()})
        if result.modified_count > 0:
            return json.dumps(user_language.dict())
        else:
            raise ValueError("Unable to update")


def delete_user_language(user_language:UserLanguage) -> str:
    if not (user_language.username):
        raise ValueError("Username missing")

    if not (user_language.language):
        raise ValueError("Language missing")

    query = {"username": user_language.username, "language": user_language.language}
    projection = {"_id": 0}
    info_from_db = profile_languages.find_one(query)

    if (info_from_db is None):
        raise ValueError("User language info does not exist")

    result = profile_languages.delete_one(query)
    if result.deleted_count:
        del info_from_db["_id"]
        return json.dumps(info_from_db)
    else:
        raise ValueError("User optional cannot be deleted")

