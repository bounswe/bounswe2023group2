import boto3
from pprint import pprint
import pathlib
import os
import config
def upload_file_using_client(destdir: str, local_file_name:str):
    """
    Uploads file to S3 bucket using S3 client object
    :return: url for the uploaded file
    """
    s3 = boto3.client("s3", 
                aws_access_key_id=config.AWS_ACCESS_KEY_ID,
                aws_secret_access_key=config.AWS_SECRET_ACCESS_KEY)
    bucket_name = 'files.practice-app.online'
    object_name = local_file_name
    file_name = os.path.join(destdir, local_file_name)

    response = s3.upload_file(file_name, bucket_name, object_name)
    return (f'{bucket_name}/{local_file_name}')
    
def get_file(file):

  s3 = boto3.client("s3", 
                aws_access_key_id=config.AWS_ACCESS_KEY_ID,
                aws_secret_access_key=   config.AWS_SECRET_ACCESS_KEY)
  bucket_name = 'files.practice-app.online'
  object_name = "deneme.txt"
  file_name = os.path.join(pathlib.Path(__file__).parent.resolve(), "deneme.txt")
  print(f'url: https://files.practice-app.online/{file}')
  # response = s3.upload_file(file_name, bucket_name, object_name)

# upload_file_using_client()
get_file('deneme.txt')