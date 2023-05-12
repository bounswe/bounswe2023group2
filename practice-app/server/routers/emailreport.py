import json
import os
from sendgrid import SendGridAPIClient
from sendgrid.helpers.mail import Mail
from fastapi import APIRouter, FastAPI, Path
from database.mongo import MongoDB
from database.baseSchema import BaseSchema
router = APIRouter()

db = MongoDB.getInstance()

@router.get("/", )
def send_mail(reporter: str, activity: str, reason: str, details: str):

    message = Mail(
        from_email = 'halil.gurbuz@boun.edu.tr',
        to_emails = 'halil.gurbuz@boun.edu.tr',
        subject = 'Report: ' + activity,
        html_content = '<html><head><style type="text/css">body, p, div { font-family: Helvetica, Arial, sans-serif; font-size: 14px;}\
        a {text-decoration: none;}</style><title>Report Info</title></head><body><center><p>\
        <body>\
        <center>\
        <p><strong>Reporter: </strong>' + reporter + '</p>\
        <p><strong>Activity ID: </strong></p>' + activity + '</p>\
        <p><strong>Reason: </strong>' + reason + '</p>\
        <p><strong>Details: </strong>' + details + '</p>\
        <p><a href="https://www.google.com"\
        style="background-color:#ffbe00; color:#000000; display:inline-block; padding:12px 40px 12px 40px; text-align:center; text-decoration:none;" \
        target="_blank">Review Report</a></p></center></body></html>')
    try:

        sg = SendGridAPIClient(api_key = os.environ.get('SENDGRID_API_KEY'))
        response = sg.send(message)
        print(response.status_code)
        print(response.body)
        print(response.headers)
        return response

    except Exception as e:
        print(e)
        return e