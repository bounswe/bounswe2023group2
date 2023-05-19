import json
import os
from sendgrid import SendGridAPIClient
from sendgrid.helpers.mail import Mail
from fastapi import APIRouter, FastAPI, Path
from database.mongo import MongoDB
from config import Config

router = APIRouter()

@router.get("/", )
def emailreport(reporter: str, activity: str, reason: str, details: str):
    
    message = Mail(
        from_email = 'halil.gurbuz@boun.edu.tr',
        to_emails = 'halil.gurbuz@boun.edu.tr',
        subject = 'Report: ' + activity,
        html_content = '<html><head><style type="text/css">body, p, div { font-family: Helvetica, Arial, sans-serif; font-size: 14px;}\
        a {text-decoration: none;}</style><title>Report Info</title></head><body><center><p>\
        <body>\
        <center>\
        <p><strong>Reporter: </strong>' + reporter + '</p>\
        <p><strong>Activity ID: </strong>' + activity + '</p>\
        <p><strong>Reason: </strong>' + reason + '</p>\
        <p><strong>Details: </strong>' + details + '</p>\
        <p><a href="https://www.google.com"\
        style="background-color:#ffbe00; color:#000000; display:inline-block; padding:12px 40px 12px 40px; text-align:center; text-decoration:none;" \
        target="_blank">Review Report</a></p></center></body></html>')
    try:
        sg = SendGridAPIClient(Config.SENDGRID_API_KEY)
        response = sg.send(message)
        print(response.status_code)
        print(response.body)
        print(response.headers)

        # db = MongoDB.getInstance()
        # reportDB = db.get_collection("reports")
        # reportDB.insert_one({"reporter": reporter, "activity": activity, "reason": reason, "details": details})
        return response
    
    except Exception as e:
        print(e)
        return e