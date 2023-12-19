import boto3
from pprint import pprint
import pathlib
import os
import config

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
