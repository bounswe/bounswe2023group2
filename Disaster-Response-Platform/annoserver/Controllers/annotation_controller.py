import json
from http import HTTPStatus

from fastapi import APIRouter, HTTPException, Response, Depends, Body
from starlette import status

# Change history:
# Created using resource as a template

router = APIRouter()