import json

def create_json_for_string(data: str, tag: str):
    return f"\"{tag}\": \"{data}\""

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
