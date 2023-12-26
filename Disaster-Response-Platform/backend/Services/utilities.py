import datetime
def correctDates(anyDictable):
    invert_op = getattr(anyDictable, "dict", None)
    if callable(invert_op):
        return_dict = anyDictable.dict()
        for key, value in return_dict.items():
            if isinstance(value, datetime.datetime):
                if value is not None:
                    pass
                    #return_dict[key] = datetime.datetime.strptime(str(return_dict[key]), "%Y-%m-%d %H:%M:%S")
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

def validate_coordinates(x=None, y=None):
    if (x is not None and ((x < -90 or x > 90)) or (y is not None and (y < -180 or y > 180))):
        raise ValueError("X coordinates should be within -90 and 90, and y coordinates within -180 and 180")

def validate_quantities(initial_quantity=None, unsupplied_quantity=None):
    if (initial_quantity is not None and (initial_quantity <= 0)) or (unsupplied_quantity is not None and (unsupplied_quantity <= 0)):
        raise ValueError("Quantities can't be less than or equal to 0")   

def set_Nones_to_old_values(dict_with_Nones:dict, dict_with_Olds:dict):
    for key, value in dict_with_Nones.items():
        if (value is None):
            dict_with_Nones[key] = dict_with_Olds[key]
    return