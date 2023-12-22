from typing import Union, List, Optional
from pydantic import BaseModel, EmailStr, AnyUrl, json, Field


import datetime

class Link(BaseModel):
    href: str
    type: Optional[str]
class Document(BaseModel):
    title: Optional[str]  # Must be one of "Text", "Image", "Data"
    link: Optional[List[Link]]

class Body(BaseModel):
    type: str  # Must be one of "Text", "Image", "Data"
    value: Union[str, AnyUrl]  # Adjust based on specific data types for each body type

class Selector(BaseModel):
    type: str  # e.g., "XPath", "CSS", "TEI"
    value: str
class Target(BaseModel):
    id: Optional[str]
    selector: Optional[List[Selector]] # For specific targeting within resources (optional)
    source: Optional[AnyUrl]  # Source of the target resource (optional)
    type: str  # Required, must be one of "Text", "Image", "Data"
    # Specify URL support based on type
    url: Optional[AnyUrl]

class Motivation(BaseModel):
    type: str  # e.g., "describing", "classifying", "tagging"

class Annotation(BaseModel):
    id: str
    body: Optional[Body]
    text:Optional[str]
    uri:AnyUrl
    document: Optional[Document]
    target: List[Target]
    motivation: Motivation
    creator: Optional[EmailStr]
    created: Optional[datetime.datetime]  # Adjust import as needed
    modified: Optional[datetime.datetime]
    tags: Optional[List[str]]
    group:str = Field(default="q1VEo531")
    references: Optional[List[AnyUrl]]  # Links to related annotations or resources

class SimpleAnnotationCreated(Body):
    id: str
    uri: AnyUrl
    tags: Optional[List[str]]

def create_annotation_simple(annotation_text: str, refUrl: AnyUrl, imageUrl: AnyUrl, tags:List[str]):
    annoBody = annotation_text + "\n\n"
    if refUrl is not None:
        annoBody += "[Click for reference URL](" + refUrl + ")\n\n"
    if imageUrl is not None:
        annoBody += "[Click for photo/image](" + imageUrl + ")"

    return annoBody

