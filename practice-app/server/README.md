## Getting Started

- python version: 3.11 (you can use +3.6 but it would be good to use same version)
- It is good to use environment for python projects
- it is good to create environment inside <b> practice-app <b> folder

```bash
python3 -m venv myenv
source myenv/bin/activate
```
- You can install requirements via:
```bash
pip3 install -r requirements.txt
```
- when you add a new library please do:

```bash
pip3 freeze > requirements.txt
```
- Config.example.py contains secrets and environment variables, you should create config.py file on the same directory. If you want to initialize a new environment variable, you can declare it on config.py


- To run code you should do
    uvicorn main:app --reload

- We are writing by fastAPI you can find the manual from [fastAPI](https://fastapi.tiangolo.com/).
- __init__.py files are for package initializition.
- We have `main.py` which stands for general application configurations and server init.
- We include the routes(endpoints) in main file. I have added `user` route as an example
- You should create your own file inside `routes` folder. You can check example user.py.

### Mongo usage
- I have added an example in user.py
- You first need to call 
  ```
  db = MongoDB.getInstance()
  ```
- collection is similar to table in sql, if you want to use the collection of user then, you should declare that like
```
userDb = db.get_collection("user")
``` 
- if you want to create new collection you can simply write
```
  userDb = db.get_collection("needs")
```

- If you create a new collection:
```
class UserSchema(BaseSchema):
	fields = {
			"username": str,
			"email": str,
			"password": str,
			}
```
- you should create schema under database folder.

### Mongo installation
- There are two options for local :
  - mongo in docker
  ```
  docker pull mongo:latest
  docker run -d -p 27017:27017 mongo:latest
  ```
  - you can use mongodb compass  for user friendly database.
  - you should create database named "practiceApp" at first.
- you can download mongo with brew or apt-get ... or with dmg file. You can find it in [mongo](https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-os-x/)

