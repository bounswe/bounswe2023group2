class Config:
    MONGO_USERNAME = ""
    MONGO_PASSWORD = ""
    MONGO_HOST = "mongo"
    MONGO_PORT = 27017
    MONGO_URI = "mongodb://" + MONGO_HOST + \
        ":" + str(MONGO_PORT) + "/my_database"
    TIMEZONE_API_KEY = ""
    BACKEND_URL = "https://practice-app.online/api"
    FCM_CREDENTIALS="" 
    FCM_AUTHORIZATION_KEY=""
    MY_FCM_KEY=""
    FCM_PROJECT_ID=""
    LOCAL_DEVICE_TOKEN_FOR_TEST=""
    NEWS_API_KEY=""
    CONSUMER_KEY = ''
    CONSUMER_SECRET = ''
    ACCESS_TOKEN = ''
    ACCESS_TOKEN_SECRET = ''
    SENDGRID_API_KEY =  ''