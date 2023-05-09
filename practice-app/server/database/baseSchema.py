class BaseSchema():
    fields = {
        "_id": str,
    }
    
    @staticmethod
    def create_object(data, **kwargs):
        return data

    @staticmethod
    def dump(d) -> dict:
        # del d["_id"]
        if '_id' in d:
            d["_id"] = str(d["_id"])
        print(d)
        return d
