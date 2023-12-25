from pydantic import BaseModel, EmailStr, constr, Field
from enum import Enum, auto
from typing import Dict, Any
from typing import Optional, List


class SearchResults(BaseModel):
    results: List[Dict[str, Any]]

class RelevanceBody(BaseModel):
    search_text: str
    search_list: List[str]

class SearchList(BaseModel):
    results: List[Dict]
