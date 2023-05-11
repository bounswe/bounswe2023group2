from .returncodes import *
from TwitterAPI import TwitterAPI

from .TWAPISECRETS import *
class TwitterSpace():
    spacesID: str
    def __init__(self, spacesID):
        self.spacesID = spacesID
    def processSpace(self):
            # send back a json with error
            return self.fetchFromTwitter()

    def fetchFromTwitter(self):
        r = twitter_api.request('spaces', {'id': self.spacesID})
        print(self.spacesID)
        return r
    def tatava(self):
        json_data = {'Kod': KOD_OK }
        return json_data
def createTwitterAPI():
    api = TwitterAPI(CONSUMER_KEY,
                     CONSUMER_SECRET,
                     ACCESS_TOKEN,
                     ACCESS_TOKEN_SECRET)
    if api is None:
        print("API error")
        return None
    print("API created")
    return api

twitter_api = createTwitterAPI()


