from fastapi import APIRouter, HTTPException, Response, Depends
from fastapi.security import OAuth2PasswordBearer
from jose import JWTError, jwt
from pydantic import BaseModel
from typing import Optional
from datetime import datetime, timedelta

router = APIRouter()

# Secret key to sign and verify the JWT token
SECRET_KEY = "your-secret-key"
ALGORITHM = "HS256"
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

# User model (you would likely use a database for user data)
class User(BaseModel):
    username: str
    email: str
    password: str

# Fake user data (replace with database query)
fake_users_db = {
    "testuser": {
        "username": "testuser",
        "password": "testpassword",
        "email": "test@example.com",
    }
}

# Create a JWT token
def create_jwt_token(data: dict, expires_delta: timedelta):
    to_encode = data.copy()
    expire = datetime.utcnow() + expires_delta
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

# Verify JWT token
def verify_jwt_token(token: str = Depends(OAuth2PasswordBearer(tokenUrl="token"))):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None:
            raise HTTPException(status_code=400, detail="Could not validate credentials")
    except JWTError:
        raise HTTPException(status_code=400, detail="Could not validate credentials")
    return username

# Login route
@router.post("/token")
async def login_for_access_token(user: User):
    if user.username in fake_users_db:
        if user.password == fake_users_db[user.username]["password"]:
            access_token_expires = timedelta(minutes=30)
            access_token = create_jwt_token(data={"sub": user.username}, expires_delta=access_token_expires)
            return {"access_token": access_token, "token_type": "bearer"}
    raise HTTPException(status_code=400, detail="Incorrect username or password")

# Protected route
@router.get("/protected")
async def protected_route(current_user: str = Depends(verify_jwt_token)):
    return {"message": f"Welcome, {current_user}!"}
