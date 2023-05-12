class Config:
    MONGO_USERNAME = ""
    MONGO_PASSWORD = ""
    MONGO_HOST = "localhost"
    MONGO_PORT = 27017
    NEWS_API_KEY="d5ed720832774c4b87a7bbd537d9ea21"
    MONGO_URI = "mongodb://" + MONGO_HOST + ":" + str(MONGO_PORT) + "/my_database"
    GOOGLE_MAPS_API_KEY="AIzaSyBVGLYY6Q0tXc6YUkRrA6bZ0BqYXe0vhHY"
    TIMEZONE_API_KEY="FWS4F4NP7SXU"
    BACKEND_URL="http://127.0.0.1:8000"