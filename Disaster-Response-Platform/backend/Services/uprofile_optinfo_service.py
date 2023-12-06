from Models.user_profile_model import UserOptionalInfo
from Database.mongo import MongoDB
from Services.build_API_returns import *
import Services.utilities

profile_optional_infos = MongoDB.get_collection('user_optional_infos')

def get_user_optional_info(username:str = None) -> str:
    projection = {"_id": 0}

    query = {}


    if (username is not None):
        query = {"username": username}
    else:
        query = {}

    info_from_db = profile_optional_infos.find(query, projection)
    result = create_json_for_successful_data_fetch(info_from_db, "user_optional_infos")
    return result

def set_user_optional_info(user_optional_info: UserOptionalInfo) -> str:
    if not (user_optional_info.username):
        raise ValueError("Username missing")

    if not any([user_optional_info.Address,
               user_optional_info.education,
               user_optional_info.blood_type,
               user_optional_info.date_of_birth, user_optional_info.health_condition, user_optional_info.identity_number,
               user_optional_info.nationality]):
        raise ValueError("No data supplied for optional info")

    query = {"username": user_optional_info.username}
    info_from_db = profile_optional_infos.find_one(query)

    eradicated_info = Services.utilities.correctDates(user_optional_info)
    if (info_from_db is None):
        result = profile_optional_infos.insert_one(eradicated_info)
        if result.inserted_id:
            eradicated_info["_id"] = str(eradicated_info["_id"])
            Services.utilities.correctDates(eradicated_info)
            return json.dumps(eradicated_info)
        else:
            raise ValueError("Unable to insert info")
    else:
        Services.utilities.set_Nones_to_old_values(eradicated_info, info_from_db)
        result = profile_optional_infos.update_one(query, {"$set": eradicated_info})
        if result.modified_count > 0:
            eradicated_info = Services.utilities.correctDates(eradicated_info)
            return json.dumps(eradicated_info)
        else:
            raise ValueError("Unable to update")

def reset_user_optional_info(username:str, reset_field:str) -> str:
    query = {"username": username}
    projection = {"_id": 0}
    info_from_db = profile_optional_infos.find_one(query,projection)

    if (info_from_db is None):
        raise ValueError("User or optional info does not exist")

    if reset_field in info_from_db.keys():
        info_from_db[reset_field] = None
        result = profile_optional_infos.update_one(query, {"$set": info_from_db})
        return info_from_db
    else:
        raise ValueError("User or optional info does not exist")

def delete_user_optional_info(username:str) -> str:
    query = {"username": username}
    projection = {"_id": 0}
    info_from_db = profile_optional_infos.find_one(query)

    if (info_from_db is None):
        raise ValueError("User optional info does not exist")

    result = profile_optional_infos.delete_one(query)
    if result.deleted_count:
        info_from_db["_id"] = str(info_from_db["_id"])
        return json.dumps(info_from_db)
    else:
        raise ValueError("User optional cannot be deleted")
