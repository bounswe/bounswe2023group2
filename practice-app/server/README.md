## Getting Started

- python version: 3.11 (you can use +3.6 but it would be good to use same version)
- It is good to use environment for python projects
```bash
python3 -m venv myenv
source myenv/bin/activate
```
- You can install requirements via:
```bash
pip install -r requirements.txt
```
- To run code you should do
    uvicorn main:app --reload
    
- We are writing by fastAPI you can find the manual from [fastAPI](https://fastapi.tiangolo.com/).
- __init__.py files are for package initializition.
- We have `main.py` which stands for general application configurations and server init.
- We include the routes(endpoints) in main file. I have added `user` route as an example
- You should create your own file inside `routes` folder. You can check example user.py.
  