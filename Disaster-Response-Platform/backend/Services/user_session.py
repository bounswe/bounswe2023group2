from passlib.apps import postgres_context, phpass_context
from datetime import datetime
from Database.mongo import MongoDB

def create_session_token(username: str, password: str):
    hash = postgres_context.hash(password, user=username)
    date_now = datetime.now()
    date_string = str(date_now)
    hash = postgres_context.hash(hash, user=date_string)

    # Here we will insert hash into the user_sessions collection
    user_session = {
        "username": username,
        "start_date": date_now,
        "token": hash
    }

    db = MongoDB.getInstance()

    user_session_file = db.get_collection("user_sessions")

    #TODO check error data etc
    session_result = user_session_file.insert_one(user_session)

    return hash