## Getting Started

- python version: 3.11 (you can use +3.6 but it would be good to use same version)
- it is good to create environment inside <b> backend <b> folder in name <b> myenv </b>

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
- To run code you should do
    uvicorn main:app --reload

- We are writing by fastAPI you can find the manual from [fastAPI](https://fastapi.tiangolo.com/).
- __init__.py files are for package initializition.
- We have `main.py` which stands for general application configurations and server init.
- We include the routes(endpoints) in main file. 
- The API service functions will be placed in files under `Services` folder.

- To run the backend in docker:
docker-compose up -d --build backend

- To run tests
pytest "test file"
