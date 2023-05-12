
class NotificationSchema(BaseSchema):
	fields = {
            "notifID": str,
         			"clientToken": str,
         			"message": str,
        }

	@staticmethod
	def create_object(data, **kwargs):
		return data


class SubscriptionSchema(BaseSchema):
	fields = {
            "clientToken": str,
         			"topicID": str,
        }

	@staticmethod
	def create_object(data, **kwargs):
		return data


class TopicSchema(BaseSchema):
	fields = {
            "Type": str,
         			"Subtype": str,
         			"City": str,
        }

	@staticmethod
	def create_object(data, **kwargs):
		return data
