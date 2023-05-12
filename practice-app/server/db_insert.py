from database.mongo import MongoDB
import random





db = MongoDB.getInstance()
def user_create():
    username = ["xxx","yyy","zzz","ccc"]
    user_db=db.get_collection("user")
    name = random.choice(username)
    x = random.uniform(37,40)
    y = random.uniform(37,40)
    user_db.insert_one({"username": name,"x_coord":x,"y_coord":y})



user_create()