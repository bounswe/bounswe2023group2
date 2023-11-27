from pydantic import BaseModel, Field
from typing import Dict, Any
from enum import Enum

class StatusTypeEnum(str, Enum):
    rejected = "rejected"
    accepted = "accepted"
    undefined = "undefined"
    
class ReportTypeEnum(str, Enum):
    users = "users"
    needs = "needs"
    resources = "resources"
    actions = "actions"
    events = "events"

class Report(BaseModel):
    _id: str = Field(default=None)
    created_by: str = Field(default=None)
    description: str = Field(default=None)
    report_type: ReportTypeEnum = Field(default=None)
    report_type_id: str = Field(default=None)
    details: Dict[str, Any] = Field(default=None)
    status: StatusTypeEnum = Field(default="undefined")
    
class AcceptReport(BaseModel):
    report_id: str = Field(default=None)
    report_type: ReportTypeEnum = Field(default=None)
    report_type_id: str = Field(default=None)
    

