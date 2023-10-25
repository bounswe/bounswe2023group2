import json
import pymongo
from pymongo import MongoClient

def create_json_for_cursor(data: pymongo.cursor, tag: str):
    reslist = list(data)
    json_data = json.dumps(reslist, default=str)
    list_string = f"\"{tag}\":" + str(json_data)
    return list_string
def create_json_for_string(data: str, tag: str):
    return f"\"{tag}\": \"{data}\""

def create_json_for_successful_data_fetch(data: pymongo.cursor, datatag: str):
    data_list = create_json_for_cursor(data, datatag)
    result_list = f"{data_list}"
    result_list = "{" + result_list + "}"
    return result_list

def create_json_for_simple(data: str, datatag: str):
    data_result = create_json_for_string(data, datatag)
    result_list = f"{data_result}"
    result_list = "{" + result_list + "}"
    return result_list
def create_json_for_error(error_message: str, error_detail: str):
    error_m1 = create_json_for_string(error_message, "ErrorMessage")
    error_m2 = create_json_for_string(error_detail, "ErrorDetail")
    result_list = f"{error_m1}, {error_m2}"
    result_list = "{" + result_list + "}"
    return result_list

def test_this():
    address = "localhost"
    port = 27017
    db_url = f"mongodb://{address}:{port}/"
    db = MongoClient(db_url).darp_db
    projection = {"_id": 0}
    user_file = db.get_collection("users")
    results = user_file.find({}, projection)
    result_list: str = create_json_for_successful_data_fetch(results, "user")
    return result_list


# Sample result for test_this:
sample_result = {
  "status": "OK",
  "user": [
    {
      "password": "",
      "username": "mehmetk"
    },
    {
      "password:": "",
      "username": "deprembaba"
    }
  ]
}
