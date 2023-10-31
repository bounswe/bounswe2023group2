import datetime

from Models.user_profile_model import UserOptionalInfo
from Models.user_profile_model import UserSocialMediaLink
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from Services.build_API_returns import *

profile_socialmedias = MongoDB.get_collection('user_socialmedias')

#def set update fields list will be prep

def set_Nones_to_old_values(dict_with_Nones:dict, dict_with_Olds:dict):
    for key, value in dict_with_Nones.items():
        if (value is None):
            dict_with_Nones[key] = dict_with_Olds[key]

def get_user_social_media(username:str = None, platform_name:str = None) -> str:
    projection = {"_id": 0}

    query = {}


    if (username is not None):
        if platform_name is not None:
            query = {"username": username, "platform_name": platform_name}
        else:
            query = {"username": username}
    else:
        if platform_name is not None:
            query = {"platform_name": platform_name}
        else:
            query = {}

    info_from_db = profile_socialmedias.find(query, projection)
    result = create_json_for_successful_data_fetch(info_from_db, "user_socialmedia_links")
    return result

def add_user_socialmedia(user_socialmedia:UserSocialMediaLink) -> str:
    if not (user_socialmedia.username):
        raise ValueError("Username missing")

    if not all([user_socialmedia.platform_name, user_socialmedia.profile_URL]):
        raise ValueError("Platform name or profile link is missing")

    query = {"username": user_socialmedia.username, "platform_name": user_socialmedia.platform_name}
    info_from_db = profile_socialmedias.find_one(query)

    if (info_from_db is None):
        result = profile_socialmedias.insert_one(dict(user_socialmedia))
        if result.inserted_id:
            return json.dumps(dict(user_socialmedia))
        else:
            raise ValueError("Unable to insert social media link for user")
    else:
        result = profile_socialmedias.update_one(query, {"$set": user_socialmedia.dict()})
        if result.modified_count > 0:
            return json.dumps(user_socialmedia.dict())
        else:
            raise ValueError("Unable to update social media link")

def delete_user_language(user_socialmedia:UserSocialMediaLink) -> str:
    if not (user_socialmedia.username):
        raise ValueError("Username missing")

    if not (user_socialmedia.platform_name):
        raise ValueError("Social media platform missing")

    query = {"username": user_socialmedia.username, "platform_name": user_socialmedia.platform_name}
    projection = {"_id": 0}
    info_from_db = profile_socialmedias.find_one(query)

    if (info_from_db is None):
        raise ValueError("User social media link info does not exist")

    result = profile_socialmedias.delete_one(query)
    if result.deleted_count:
        del info_from_db["_id"]
        return json.dumps(info_from_db)
    else:
        raise ValueError("User social media cannot be deleted")
