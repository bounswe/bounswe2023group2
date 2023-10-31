import datetime

from Models.user_profile_model import UserOptionalInfo
from Models.user_profile_model import UserProfession
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from Services.build_API_returns import *

profile_professions = MongoDB.get_collection('user_professions')

#def set update fields list will be prep

def set_Nones_to_old_values(dict_with_Nones:dict, dict_with_Olds:dict):
    for key, value in dict_with_Nones.items():
        if (value is None):
            dict_with_Nones[key] = dict_with_Olds[key]

def get_user_profession(username:str = None, profession:str = None) -> str:
    projection = {"_id": 0}

    query = {}


    if (username is not None):
        if profession is not None:
            query = {"username": username, "profession": profession}
        else:
            query = {"username": username}
    else:
        if profession is not None:
            query = {"profession": profession}
        else:
            query = {}

    info_from_db = profile_professions.find(query, projection)
    result = create_json_for_successful_data_fetch(info_from_db, "user_professions")
    return result

def add_user_profession(user_profession:UserProfession) -> str:
    if not (user_profession.username):
        raise ValueError("Username missing")

    if not all([user_profession.profession,user_profession.profession_level]):
        raise ValueError("Level or profession missing")

    query = {"username": user_profession.username, "profession": user_profession.profession}
    info_from_db = profile_professions.find_one(query)

    if (info_from_db is None):
        result = profile_professions.insert_one(dict(user_profession))
        if result.inserted_id:
            return json.dumps(dict(user_profession))
        else:
            raise ValueError("Unable to insert profession")
    else:
        result = profile_professions.update_one(query, {"$set": user_profession.dict()})
        if result.modified_count > 0:
            return json.dumps(user_profession.dict())
        else:
            raise ValueError("Unable to update")


def delete_user_profession(user_profession: UserProfession) -> str:
    if not (user_profession.username):
        raise ValueError("Username missing")

    if not (user_profession.profession):
        raise ValueError("profession missing")

    query = {"username": user_profession.username, "profession": user_profession.profession}

    info_from_db = profile_professions.find_one(query)

    if (info_from_db is None):
        raise ValueError("User profession info does not exist")

    result = profile_professions.delete_one(query)
    if result.deleted_count:
        del info_from_db["_id"]
        return json.dumps(info_from_db)
    else:
        raise ValueError("User profession info cannot be deleted")

