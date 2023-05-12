from requests_oauthlib import OAuth1Session

from .returncodes import *
from .TWAPISECRETS import *
class TwitterFunc():
    systemUser: str
    def __init__(self, systemUser):
        self.systemUser = systemUser
    def processEventForTweet(self, event_id):

            return self.createTweet(f"#Urgent-{self.systemUser}", event_id)

    def createTweet(self,message, event_id):
        tweet_text = f'Please check the need information on our site\n#{event_id}\n{message}'

        try:
            params = {"text": tweet_text}
            response = tw_session.post('https://api.twitter.com/2/tweets', json=params)
            if response.status_code == 200 or response.status_code == 201:
                json_response = response.json()
                twid = json_response['data']['id']
                return {"URL": f"https://twitter.com/user/status/{twid}"}
            else:
                return {"URL": "https://twitter.com/", "ERROR": f"{response.status_code}"}
        except Exception as e:
            print(e.with_traceback(None))
            print(e.args)
            return {"URL": "https://twitter.com/", "ERROR": f"{e}"}

def getTwitterSession():
    oauth = OAuth1Session(
        CONSUMER_KEY,
        client_secret=CONSUMER_SECRET,
        resource_owner_key=ACCESS_TOKEN,
        resource_owner_secret=ACCESS_TOKEN_SECRET,
    )
    return oauth

tw_session = getTwitterSession()
