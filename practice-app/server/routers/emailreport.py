import json
import os
from sendgrid import SendGridAPIClient
from sendgrid.helpers.mail import Mail
from fastapi import APIRouter, FastAPI, Path, HTTPException
from database.mongo import MongoDB
from config import Config
from pydantic import BaseModel

class Mail_Type(BaseModel):
    email: str

    
class Report_Type(BaseModel):
    reporter: str
    activity: str
    reason: str
    details: str


router = APIRouter()

@router.post('/add_admin')
def add_admin(email: Mail_Type):
    db = MongoDB.getInstance()
    adminDB = db.get_collection("admins")
    adminDB.insert_one({"username": "admin4report",
                       "first_name": "admin",
                       "last_name": "istrator",
                       "email": email.email,
                       "phone_number": "+905554443322",
                       "password": "1234567890"})
    return {
        "username": "admin4report",
        "first_name": "admin",
        "last_name": "istrator",
        "email": email.email,
        "phone_number": "+905554443322"}


@router.get('/get_admin')
def get_admin():
    db = MongoDB.getInstance()
    adminDB = db.get_collection('admins')
    result = adminDB.find_one({'username': 'admin4report'})
    if result is None:
        raise HTTPException(status_code=404, detail="Admin not found in the DB")
    return {"username": result['username'],
            "first_name": result['first_name'],
            "last_name": result['last_name'],
            "email": result['email'],
            "phone_number": result['phone_number']
            }


@router.post('/')
def emailreport(report: Report_Type):

    admin_email = get_admin()['email']

    message = Mail(
        from_email = 'halil.gurbuz@boun.edu.tr',
        to_emails = admin_email,
        subject = 'Report: ' + report.activity,
        html_content = '<html><head><style type="text/css">body, p, div { font-family: Helvetica, Arial, sans-serif; font-size: 14px;}\
        a {text-decoration: none;}</style><title>Report Info</title></head><body><center><p>\
        <body>\
        <center>\
        <p><strong>Reporter: </strong>' + report.reporter + '</p>\
        <p><strong>Activity ID: </strong>' + report.activity + '</p>\
        <p><strong>Reason: </strong>' + report.reason + '</p>\
        <p><strong>Details: </strong>' + report.details + '</p>\
        <p><a href="https://www.google.com"\
        style="background-color:#ffbe00; color:#000000; display:inline-block; padding:12px 40px 12px 40px; text-align:center; text-decoration:none;" \
        target="_blank">Review Report</a></p></center></body></html>')
    try:
        sg = SendGridAPIClient(Config.SENDGRID_API_KEY)
        response = sg.send(message)
        print(response.status_code)
        print(response.body)
        print(response.headers)

        db = MongoDB.getInstance()
        reportDB = db.get_collection("reports")
        reportDB.insert_one({"reporter": report.reporter, "activity": report.activity, "reason": report.reason, "details": report.details})
        return report
    
    except Exception as e:
        print(e)
        return e