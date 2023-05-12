import oauth2
import json
import requests

from .returncodes import *
from .TWAPISECRETS import *
class TwitterSpace():
    spacesID: str
    def __init__(self, spacesID):
        self.spacesID = spacesID
    def processSpace(self):
            # send back a json with error
            return self.fetchFromTwitter()

    def fetchFromTwitter(self):
        args = {"ids": "1278747501642657792"}

        #url = 'https://api.twitter.com/2/tweets'
        url = f'https://api.twitter.com/2/spaces/{self.spacesID}'
        url ='https://api.twitter.com/2/tweets?ids=1228393702244134912,1227640996038684673,1199786642791452673&tweet.fields=created_at&expansions=author_id&user.fields=created_at'
        print(url)
        client = getAuthedClientForTwitter()
        response, data = client.request(uri=url)
        print(response)
        print(data)
        return data

def bearer_oauth(r):
    """
    Method required by bearer token authentication.
    """

    r.headers["Authorization"] = f"Bearer {TWITTER_TOKEN}"
    r.headers["User-Agent"] = "v2Group2CMPE"
    return r

def getAuthedClientForTwitter():
    consumer = oauth2.Consumer(key=CONSUMER_KEY, secret=CONSUMER_SECRET)
    access_token = oauth2.Token(key=ACCESS_TOKEN, secret=ACCESS_TOKEN_SECRET)
    client = oauth2.Client(consumer,access_token)
    print (client.connections.values())
    return client
