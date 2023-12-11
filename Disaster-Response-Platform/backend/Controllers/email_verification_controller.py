# imports

router = APIRouter()

@router.post("/send", status_code=200)
async def send_verification_email(username: str = Depends(authentication_service.get_current_username)):
    return 

@router.post("/verify", status_code=200)
async def verify_email():
    return