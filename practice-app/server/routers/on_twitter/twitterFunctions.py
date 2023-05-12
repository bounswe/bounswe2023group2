import tweepy
import json
import requests
from TwitterSpaces2Text.Extractor import Extractor

from .returncodes import *
from .TWAPISECRETS import *
class TwitterSpace():
    spacesID: str
    def __init__(self, spacesID):
        self.spacesID = spacesID
    def processSpace(self):
            # send back a json with error
            return self.fetchFromTwitter(10)

    def fetchFromTwitter(self,count):
        #r = tweepy_client.get_users_tweets(44196397)
        tweepy_client.create_tweet(text="This is to announce a test")
        return {"loli": "LALA"}

def beginTClient():
    client = tweepy.Client(
        consumer_key=CONSUMER_KEY, consumer_secret=CONSUMER_SECRET,
        access_token=ACCESS_TOKEN, access_token_secret=ACCESS_TOKEN_SECRET
    )
    return client
def beginTSession():
    auth = tweepy.OAuthHandler(CONSUMER_KEY,CONSUMER_SECRET)
    auth.set_access_token(ACCESS_TOKEN,ACCESS_TOKEN_SECRET)

    api = tweepy.API(auth)

    if api is None:
        return None
    return api

twitter_api = beginTSession()
tweepy_client = beginTClient()
