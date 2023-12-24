import boto3
from pprint import pprint
from fastapi import Query, APIRouter, HTTPException, Response, Depends, Body
from typing import List, Optional
import pathlib
import os
import config
import json
from Services.resource_service import get_resources
from Services.need_service import get_needs

s3 = boto3.client("s3", 
                aws_access_key_id=config.AWS_ACCESS_KEY_ID,
                aws_secret_access_key=config.AWS_SECRET_ACCESS_KEY)

def upload_file_using_client(file):
    try:
      bucket_name = 'files.practice-app.online'
      response = s3.upload_file("./tmp/"+file, bucket_name, file)
      return (f'https://{bucket_name}/{file}')
    except Exception as e:
      print(e)
      return {"message": "S3_UPLOAD_ERROR"}
def delete_file_from_s3(file):
    try:
        bucket_name = 'files.practice-app.online'
        print(bucket_name)
        s3.delete_object(Bucket=bucket_name, Key=file)
        return {"message": f"{file} deleted successfully"}
    except Exception as e:
        print(e)
        return {"message": "S3_DELETE_ERROR"}

def json_to_html(json_data, activity):
    html = """
    <html>
    <head>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ddd; /* Light gray border */
            }
            th {
                background-color: #007bff; /* Bootstrap primary blue */
                color: white;
            }
            tr:nth-child(even) {
                background-color: #f2f2f2; /* Light gray for even rows */
            }
            tr:hover {
                background-color: #ddd; /* Slightly darker gray for hover */
            }
        </style>
    </head>
    <body>
        <table>
    """

    # Add table headers
    headers=0
    if activity=="Resource":
      headers = json_data["resources"][0].keys()
      html += "<tr>" + "".join(f"<th>{header}</th>" for header in headers) + "</tr>"
      for resource in json_data["resources"]:
        html += "<tr>" + "".join(f"<td>{resource.get(key, '')}</td>" for key in headers) + "</tr>"

    elif activity=="Need":
      headers = json_data["needs"][0].keys()
      html += "<tr>" + "".join(f"<th>{header}</th>" for header in headers) + "</tr>"
      for need in json_data["needs"]:
        html += "<tr>" + "".join(f"<td>{need.get(key, '')}</td>" for key in headers) + "</tr>"

    
    # Add table rows

    html += "</table></body></html>"
    return html

def create_and_upload_file(    
    active: Optional[bool] = None,
    types: list = None, 
    subtypes: list = None, 
    x: float = None,
    y: float = None,
    distance_max: float = None,
    sort_by: str = 'created_at',
    order: Optional[str] = 'desc',
    activity_type: str = None

):
    html_data=0
    filename = "filtered_resources.html"
    if activity_type=="Resource":
      resources= get_resources(
              active=active,
              types=types,
              subtypes=subtypes,
              x=x,
              y=y,
              distance_max=distance_max,
              sort_by=sort_by,
              order=order
          )
      resources= json.loads(resources)
      html_data = json_to_html(resources, activity_type)
    elif activity_type=="Need":
      needs= get_needs(
              active=active,
              types=types,
              subtypes=subtypes,
              x=x,
              y=y,
              distance_max=distance_max,
              sort_by=sort_by,
              order=order
          )
      needs= json.loads(needs)
      html_data = json_to_html(needs, activity_type)   
      print("buraya giriyon dimi")
      filename = "filtered_needs.html"     
        
      # Replace with your desired file name
    with open(filename, "w") as file:
        file.write( html_data)

    # Step 3: Upload the file to S3
    try:
        bucket_name = 'files.practice-app.online'
        response = s3.upload_file(filename, bucket_name, filename)
        file_url = f'https://{bucket_name}.s3.amazonaws.com/{filename}'
        return {"url": file_url}
    except Exception as e:
        print(e)
        return {"message": "S3_UPLOAD_ERROR"}
