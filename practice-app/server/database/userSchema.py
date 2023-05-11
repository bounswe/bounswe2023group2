
from database.baseSchema import BaseSchema


class UserSchema(BaseSchema):
	fields = {
			"username": str,
			"email": str,
			"password": str,
			"x_coord": float,
			"y_coord": float
			}

	@staticmethod
	def create_object(data, **kwargs):
		return data
	