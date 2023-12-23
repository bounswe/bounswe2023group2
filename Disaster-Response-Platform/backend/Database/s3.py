import boto3
from pprint import pprint
from fastapi import Query, APIRouter, HTTPException, Response, Depends, Body
from typing import List, Optional
import pathlib
import os
import config
import json
from Services.resource_service import *

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


def create_and_upload_file(    
    resource_id: str = None,
    active: Optional[bool] = None,
    types: list = None, 
    subtypes: list = None, 
    x: float = None,
    y: float = None,
    distance_max: float = None,
    sort_by: str = 'created_at',
    order: Optional[str] = 'desc'

):

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
    resources_json = json.dumps(resources)

    filename = "filtered_resources.json"  # Replace with your desired file name
    with open(filename, "w") as file:
        file.write(resources_json)

    # Step 3: Upload the file to S3
    try:
        bucket_name = 'files.practice-app.online'
        response = s3.upload_file(filename, bucket_name, filename)
        file_url = f'https://{bucket_name}.s3.amazonaws.com/{filename}'
        return {"url": file_url}
    except Exception as e:
        print(e)
        return {"message": "S3_UPLOAD_ERROR"}
