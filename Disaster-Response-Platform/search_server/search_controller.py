from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer, util
from typing import List
import requests

# Initialize the model
model = SentenceTransformer('multi-qa-mpnet-base-cos-v1')

# Define the app
app = FastAPI()

# Request model
class RelevanceRequest(BaseModel):
    search_text: str
    search_list: List[str]

# Endpoint for calculating relevance
@app.post("/relevance")
def relevance(request: RelevanceRequest):
    try:
        query_embedding = model.encode(request.search_text)
        search_list_embedding = model.encode(request.search_list)
        res_search_list = util.dot_score(query_embedding, search_list_embedding)

        return {
            "relevances": res_search_list[0].tolist()
        }
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Something is wrong with: {str(e)}")

# Run the app
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=9060)
