class Config:
    MONGO_USERNAME = ""
    MONGO_PASSWORD = ""
    MONGO_HOST = "localhost"
    MONGO_PORT = 27017
    MONGO_URI = "mongodb://" + MONGO_HOST + ":" + str(MONGO_PORT) + "/my_database"
    TIMEZONE_API_KEY="FWS4F4NP7SXU"
    BACKEND_URL="http://127.0.0.1:8000"