from fastapi import APIRouter, Depends, HTTPException, Body, Query
from Services.email_verification_service import send_verification_email, verify_user, check_verified
from Services.authentication_service import get_current_username, get_current_email

router = APIRouter()

@router.post("/send", status_code=200)
async def send_verification_request(username: str = Depends(get_current_username), email: str = Depends(get_current_email)):
    success = send_verification_email(username, email)
    if success:
        return {"message": "Verification email sent."}
    else:
        raise HTTPException(status_code=500, detail="Failed to send verification email")

@router.post("/verify", status_code=200)
async def verify_email(token: str = Query(None, description="Verification token obtained from email"), username: str = Depends(get_current_username)):
    if verify_user(username, token):
        return {"message": "Email verified successfully."}
    else:
        raise HTTPException(status_code=400, detail="Invalid token or token expired")

@router.get("/check", status_code=200)
async def check_verification_of_user(username: str = Depends(get_current_username)):
    status = check_verified(username)
    if status == "verified":
        return {"message": "verified"}
    elif status == "not verified":
        return {"message": "not verified"}
    else:
        raise HTTPException(status_code=404, detail="user not found")


