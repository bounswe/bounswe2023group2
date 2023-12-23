import datetime

from Models.user_profile_model import UserOptionalInfo
from Models.user_profile_model import UserSkill
from Database.mongo import MongoDB
from bson.objectid import ObjectId
from Services.build_API_returns import *

profile_skills = MongoDB.get_collection('user_skills')


def get_user_skill(username:str = None, skill:str = None) -> str:
    projection = {"_id": 0}

    query = {}


    if (username is not None):
        if skill is not None:
            query = {"username": username, "skill_definition": skill}
        else:
            query = {"username": username}
    else:
        if skill is not None:
            query = {"skill_definition": skill}
        else:
            query = {}

    info_from_db = profile_skills.find(query, projection)
    result = create_json_for_successful_data_fetch(info_from_db, "user_skills")
    return result

def add_user_skill(user_skill: UserSkill) -> str:
    if not (user_skill.username):
        raise ValueError("Username missing")

    if not all([user_skill.skill_definition, user_skill.skill_level]):
        raise ValueError("Level or skill missing")

    query = {"username": user_skill.username, "skill_definition": user_skill.skill_definition}
    info_from_db = profile_skills.find_one(query)

    if (info_from_db is None):
        result = profile_skills.insert_one(dict(user_skill))
        if result.inserted_id:
            return json.dumps(dict(user_skill))
        else:
            raise ValueError("Unable to insert skill")
    else:
        result = profile_skills.update_one(query, {"$set": user_skill.dict()})
        if result.modified_count > 0:
            return json.dumps(user_skill.dict())
        else:
            raise ValueError("Unable to update")


def delete_user_skill(user_skill: UserSkill) -> str:
    if not (user_skill.username):
        raise ValueError("Username missing")

    if not (user_skill.skill_definition):
        raise ValueError("skill missing")

    query = {"username": user_skill.username, "skill_definition": user_skill.skill_definition}

    info_from_db = profile_skills.find_one(query)

    if (info_from_db is None):
        raise ValueError("User profession info does not exist")

    result = profile_skills.delete_one(query)
    if result.deleted_count:
        del info_from_db["_id"]
        return json.dumps(info_from_db)
    else:
        raise ValueError("User skill info cannot be deleted")

