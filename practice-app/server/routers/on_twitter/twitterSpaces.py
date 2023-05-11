from .returncodes import *
class TwitterSpace():
    spacesID: str
    def __init__(self, spacesID):
        self.spacesID = spacesID
    def processSpace(self):
            # send back a json with error
            return self.fetchFromTwitter()

    def fetchFromTwitter(self):
        json_data = {'Kod': KOD_OK }
        return json_data


