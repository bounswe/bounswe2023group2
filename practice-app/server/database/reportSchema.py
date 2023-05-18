
from database.baseSchema import BaseSchema


class ReportSchema(BaseSchema):
	fields = {
			"reporter": str,
			"activity": str,
			"reason": str,
			"details": str,
			}

	@staticmethod
	def create_object(data, **kwargs):
		return data
	