from fastapi import HTTPException, Response,status
from requests_oauthlib import OAuth1Session
from .returncodes import *

from config import *
from database.mongo import MongoDB
EVENTS_COLLECTION = 'events_twapi'

class TwitterFunc():
    systemUser: str
    def __init__(self, systemUser):
        self.systemUser = systemUser
    def processEventForTweet(self, event_id:int, response:Response):
            return self.createTweets(event_id, response)

    def createTweets(self, event_id:int, response:Response):
        mydb = MongoDB.getInstance()
        events = mydb.get_collection(EVENTS_COLLECTION)
        qry = { "event_id": int(event_id) }
        myevents = list(events.find(qry))

        in_reply_tweet = ""
        related_tweets = []
        eventFound = False
        for ev in myevents:
            eventFound = True
            twits = ev['related_twits']
            for twit in twits:
                response.status_code = status.HTTP_418_IM_A_TEAPOT
                return None
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
                    myResponse = tw_session.post('https://api.twitter.com/2/tweets', json=params)
                    if myResponse.status_code == 200 or myResponse.status_code == 201:
                        json_response = myResponse.json()
                        twid = json_response['data']['id']
                        in_reply_tweet = twid
                        related_tweets.append(twid)
                    else:
                        response.status_code = status.HTTP_418_IM_A_TEAPOT
                        return None
                except Exception as e:
                    print(e.with_traceback(None))
                    print(e.args)
                    response.status_code = status.HTTP_418_IM_A_TEAPOT
                    return None
            events.update_one(qry,{"$set" : {"related_twits": related_tweets }})
        if not eventFound:
            response.status_code = status.HTTP_404_NOT_FOUND
            return None
        return {'URL': f'https://twitter.com/user/status/{in_reply_tweet}'}

    def getPublishedTweets(self, event_id:int, response:Response):
        mydb = MongoDB.getInstance()
        events = mydb.get_collection(EVENTS_COLLECTION)
        qry = { "event_id": int(event_id) }
        myevents = list(events.find(qry))
        ret_value = []
        for ev in myevents:
            twits = ev['related_twits']
            for twit in twits:
                ret_value.append(f'https://twitter.com/user/status/{twit}')
            response.status_code = status.HTTP_200_OK
            return {"URLs": ret_value}
        response.status_code=status.HTTP_404_NOT_FOUND
        return {"URL": "https://twitter.com/", "ERROR": "Not twitted"}

    def getEvents(self):
        mydb = MongoDB.getInstance()
        events = mydb.get_collection(EVENTS_COLLECTION)
        myevents = list(events.find())
        ret_value = []
        key = 1
        for ev in myevents:
            event_date = ev['event_date']
            verified = ev['verified']
            event_id = ev['event_id']
            event_summary_list = ev['event_summary']
            event_summary = ""
            for event_summ in event_summary_list:
                event_summary = f'{event_summary}{event_summ}\n'

            related_twits_list = ev['related_twits']
            related_twits = ""
            for related_tw in related_twits_list:
                related_twits = f'{related_twits}{related_tw}\n'

            ret_value.append({'key':key,
                              'event_date': event_date.strftime("%m/%d/%Y %H:%M"),
                              'event_summary': event_summary_list,
                              'related_twits': related_twits_list,
                              'verified': verified,
                              'event_id': event_id
                              })
            key = key + 1
        return {'Events' : ret_value}

    def deletePublishedTweets(self, event_id:int, response:Response):
        mydb = MongoDB.getInstance()
        events = mydb.get_collection(EVENTS_COLLECTION)
        qry = { "event_id": int(event_id) }
        myevents = list(events.find(qry))
        ret_value_success = []
        ret_value_error = []
        for ev in myevents:
            twits = ev['related_twits']
            for twit in twits:
                response = tw_session.delete(f"https://api.twitter.com/2/tweets/{twit}")
                if response.status_code == 200 or response.status_code == 201:
                    json_response = response.json()
                    is_deleted = json_response['data']['deleted']
                    if is_deleted:
                        ret_value_success.append(f'https://twitter.com/user/status/{twit}')
                    else:
                        ret_value_error.append(f'https://twitter.com/user/status/{twit}')
                    continue
                else:
                    ret_value_error.append(f'https://twitter.com/user/status/{twit}')
                    continue
            events.update_one({"event_id" : int(event_id)}, { "$set": {"related_twits": []}} );
            return [{"Deleted": ret_value_success}, {"Not successfull": ret_value_error}]
        response.status_code = status.HTTP_404_NOT_FOUND
        return [{"Deleted":[]}]
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
