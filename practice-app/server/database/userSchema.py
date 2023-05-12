
from database.baseSchema import BaseSchema


class UserSchema(BaseSchema):
	fields = {
			"username": str,
			"email": str,
			"password": str,
			"timezone": str
			}

	@staticmethod
	def create_object(data, **kwargs):
		return data
	