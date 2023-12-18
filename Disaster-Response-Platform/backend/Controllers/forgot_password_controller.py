from fastapi import APIRouter, HTTPException, Depends, Body, Query
from Services.forgot_password_service import send_reset_password_email, reset_password

router = APIRouter()

@router.post("/send", status_code=200)
async def send_reset_password_verification_email(email: str = Query(None, description="Email associated with the user")):
    result = send_reset_password_email(email)
    
    if result == "Email sent successfully":
        return {"message": "Password reset verification email sent."}
    elif result == "Invalid email address":
        raise HTTPException(status_code=400, detail="Invalid email address")
    else:
        raise HTTPException(status_code=500, detail="Failed to send password reset verification email")

@router.post("/reset", status_code=200)
async def reset_user_password(email: str = Query(None, description="Email associated with the user"),
                            token: str = Query(None, description="Verification token obtained from email"), 
                            new_password: str = Query(None, description="New password")):
                            
    if reset_password(email, token, new_password):
        return {"message": "Password reset successful."}
    else:
        raise HTTPException(status_code=400, detail="Invalid token or token expired")
