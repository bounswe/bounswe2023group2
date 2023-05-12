
from database.baseSchema import BaseSchema

class LocationSchema(BaseSchema):
    fields = {
    	"latitude": float,
    	"longitude": float
    }

    @staticmethod
    def create_object(data, **kwargs):
	    return data
