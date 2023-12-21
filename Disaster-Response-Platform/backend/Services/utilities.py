import datetime
def correctDates(anyDictable):
    invert_op = getattr(anyDictable, "dict", None)
    if callable(invert_op):
        return_dict = anyDictable.dict()
        for key, value in return_dict.items():
            if isinstance(value, datetime.datetime):
                if value is not None:
                    return_dict[key] = datetime.datetime.strptime(str(return_dict[key]), "%Y-%m-%d %H:%M:%S")
            elif isinstance(value, datetime.date):
                if value is not None:
                    return_dict[key] = datetime.datetime.strptime(str(return_dict[key]), "%Y-%m-%d")
    else:
        if isinstance(anyDictable, dict):
            return_dict = anyDictable
            for key, value in return_dict.items():
                if isinstance(value, datetime.date) or isinstance(value, datetime.datetime):
                    if value is not None:
                        return_dict[key] = str(return_dict[key])
        else:
            return None
    return return_dict

def set_Nones_to_old_values(dict_with_Nones:dict, dict_with_Olds:dict):
    for key, value in dict_with_Nones.items():
        if (value is None):
            dict_with_Nones[key] = dict_with_Olds[key]
    return