from Models.report_model import Report
from Database.mongo import MongoDB
from bson.objectid import ObjectId

from Services.build_API_returns import *


# Get the reports collection using the MongoDB class
reports_collection = MongoDB.get_collection('reports')

def create_report(report: Report) -> str:
    # Manual validation for required fields during creation
    if not all([report.created_by, report.description, 
                report.report_type, report.report_type_id, report.details]):
        raise ValueError("All fields are mandatory for creation.")
    existing_report = reports_collection.find_one({"created_by": report.created_by, "report_type": report.report_type, "report_type_id": report.report_type_id}) 
    if existing_report:
        raise ValueError("Report already exists")
    insert_result = reports_collection.insert_one(report.dict())
    if insert_result.inserted_id:
        result = "{\"reports\":[{\"_id\":" + f"\"{insert_result.inserted_id}\""+"}]}"
        return result
    else:
        raise ValueError("Report could not be created")


def get_report_by_id(report_id: str) -> list[dict]:
    return get_reports(report_id)



def get_reports(report_id:str = None) -> list[dict]:
    projection = {
            "_id": {"$toString": "$_id"},
            "created_by": 1,
            "description": 1,
            "report_type": 1,
            "report_type_id": 1,
            "details": 1,
            "status": 1
        }
    
    if (report_id is None):
        query = {}
    else:
        if (ObjectId.is_valid(report_id)):
            query = {"_id": ObjectId(report_id)}
        else:
            raise ValueError(f"Report id {report_id} is invalid")

    reports_data = reports_collection.find(query, projection)

    if (reports_data.explain()["executionStats"]["nReturned"] == 0):
        if report_id is None:
            report_id = ""
        raise ValueError(f"Report {report_id} does not exist")
    result_list = create_json_for_successful_data_fetch(reports_data, "reports")
    return result_list

    
def update_report(report_id: str, report: Report) -> Report:
    existing_report = reports_collection.find_one({"_id": ObjectId(report_id)})

    if existing_report:
        # If details exist in the provided need and the database, merge them
        if 'details' in report.dict(exclude_none=True) and 'details' in existing_report:
            report.details = {**existing_report['details'], **report.dict(exclude_none=True)['details']}

        update_data = {k: v for k, v in report.dict(exclude_none=True).items()}

        reports_collection.update_one({"_id": ObjectId(report_id)}, {"$set": update_data})

        updated_report_data = reports_collection.find_one({"_id": ObjectId(report_id)})
        return Report(**updated_report_data)
    else:
        raise ValueError(f"Report id {report_id} not found")


def delete_report(report_id: str):
    try:
        d = reports_collection.delete_one({"_id": ObjectId(report_id)})
        if d.deleted_count == 0:
            raise
        return "{\"reports\":[{\"_id\":" + f"\"{report_id}\"" + "}]}"
    except:
        raise ValueError(f"Report {report_id} cannot be deleted")
    
    
def reject_report(report_id: str):
    result = reports_collection.update_one({"_id": ObjectId(report_id)}, {"$set": {"status": "rejected"}})
    if result.matched_count == 0:
        raise ValueError(f"Report id {report_id} not found")
    # return True

def accept_report(report_id: str, report_type:str, report_type_id: str) :
    result = reports_collection.update_one({"_id": ObjectId(report_id)}, {"$set": {"status": "accepted"}})
    if result.matched_count == 0:
        raise ValueError(f"Report id {report_id} not found")

    collection = MongoDB.get_collection(report_type)
    try:
        d = collection.delete_one({"_id": ObjectId(report_type_id)})
        if d.deleted_count == 0:
            raise ValueError(f"{report_type} {report_type_id} cannot be deleted")
    except:
        raise ValueError(f"{report_type} {report_type_id} cannot be deleted")  

