from requests_oauthlib import OAuth1Session

from .returncodes import *
from config import *
from database.mongo import MongoDB
EVENTS_COLLECTION = 'events_twapi'
class TwitterFunc():
    systemUser: str
    def __init__(self, systemUser):
        self.systemUser = systemUser
    def processEventForTweet(self, event_id:int):
            return self.createTweets(event_id)

    def createTweets(self, event_id:int):
        mydb = MongoDB.getInstance()
        events = mydb.get_collection(EVENTS_COLLECTION)
        print(events)
        qry = { "event_id": int(event_id) }
        print(f"\nqry={qry}\n")
        myevents = list(events.find(qry))
        print(myevents)
        in_reply_tweet = ""
        related_tweets = []
        for ev in myevents:
            twits = ev['related_twits']
            for twit in twits:
                return {"URL": "https://twitter.com/", "ERROR": f"{response.status_code}"}
            summs = ev['event_summary']
            evdate = ev['event_date']
            for summ in summs:
                print(summ)

                tweet_text = f'{summ}\n#{event_id}\n#BOUNCMPE352-DRR [{evdate}]'

                try:
                    if not (in_reply_tweet == ""):
                        params = {"text": tweet_text, "reply": {"in_reply_to_tweet_id": f"{in_reply_tweet}"}}
                    else:
                        params = {"text": tweet_text}
                    response = tw_session.post('https://api.twitter.com/2/tweets', json=params)
                    if response.status_code == 200 or response.status_code == 201:
                        json_response = response.json()
                        twid = json_response['data']['id']
                        in_reply_tweet = twid
                        related_tweets.append(twid)
                    else:
                        return {"URL": "https://twitter.com/", "ERROR": f"{response.status_code}"}
                except Exception as e:
                    print(e.with_traceback(None))
                    print(e.args)
                    return {"URL": "https://twitter.com/", "ERROR": f"{e}"}
            events.update_one(qry,{"$set" : {"related_twits": related_tweets }})
        return {'URL': f'https://twitter.com/user/status/{in_reply_tweet}'}

    def getPublishedTweets(self, event_id:int):
        mydb = MongoDB.getInstance()
        events = mydb.get_collection(EVENTS_COLLECTION)
        qry = { "event_id": int(event_id) }
        myevents = list(events.find(qry))
        ret_value = []
        for ev in myevents:
            twits = ev['related_twits']
            for twit in twits:
                ret_value.append(f'https://twitter.com/user/status/{twit}')
            return {"URLs": ret_value}
        return {"URL": "https://twitter.com/", "ERROR": "Not twitted"}

def getTwitterSession():
    oauth = OAuth1Session(
        Config.CONSUMER_KEY,
        client_secret=Config.CONSUMER_SECRET,
        resource_owner_key=Config.ACCESS_TOKEN,
        resource_owner_secret=Config.ACCESS_TOKEN_SECRET,
    )
    return oauth

def checkConnection():
    mydb = MongoDB.getInstance()
    if not mydb:
        return False
    events = mydb.get_collection(EVENTS_COLLECTION)
    for event in list(events.find()):
        return True

    return False

tw_session = getTwitterSession()
