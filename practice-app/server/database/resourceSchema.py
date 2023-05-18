from database.baseSchema import BaseSchema
from database.mongo import MongoDB

class ResourceSchema(BaseSchema):
    fields = {
        "id": str,
        "type": str,
        "location": str,
        "notes": str,
        "updated_at": str,
        "is_active": bool,
        "upvotes": int,
        "downvotes": int,
        "creator_id": str,
        "creation_date": str,
        "condition": str,
        "quantity": int
    }

    @staticmethod
    def create_object(data, **kwargs):
        return data

    @staticmethod
    def add_resource(data):
        mongo = MongoDB.getInstance()
        resources = mongo.get_collection('Resources')
        converted_data = {key: ResourceSchema.fields[key](value) for key, value in data.items()}
        if not all(key in ResourceSchema.fields and type(value) == ResourceSchema.fields[key] for key, value in converted_data.items()):
            raise ValueError("Invalid data")
        result = resources.insert_one(converted_data)
        return result.inserted_id

    @staticmethod
    def delete_resource(resource_id):
        mongo = MongoDB.getInstance()
        resources = mongo.get_collection('Resources')
        result = resources.delete_one({"id": resource_id})
        return result.deleted_count

    @staticmethod
    def clear_resources():
        mongo = MongoDB.getInstance()
        resources = mongo.get_collection('Resources')
        result = resources.delete_many({})
        return result.deleted_count

    import random
    from datetime import datetime

    @staticmethod
    def generate_random_resources(num_resources):
        resource_types = ["book", "cloth", "electronic", "furniture", "toy"]
        locations = ["Location1", "Location2", "Location3", "Location4", "Location5"]
        conditions = ["Good", "Fair", "Poor"]
        creators = ["Creator1", "Creator2", "Creator3", "Creator4", "Creator5"]

        resources = []
        for i in range(1, num_resources + 1):
            resource = {
                "id": str(i),
                "type": random.choice(resource_types),
                "location": random.choice(locations),
                "notes": f"Notes{i}",
                "updated_at": datetime.now().isoformat(),
                "is_active": random.choice([True, False]),
                "upvotes": random.randint(0, 100),
                "downvotes": random.randint(0, 10),
                "creator_id": random.choice(creators),
                "creation_date": datetime.now().isoformat(),
                "condition": random.choice(conditions),
                "quantity": random.randint(1, 10)
            }
            resources.append(resource)

        return resources