package com.example.disasterresponseplatform.data.models

class VoteBody {

    /**
     * {
    "entityType": "needs",
    "entityID": "string"
    }
     */
    data class VoteRequestBody(
        val entityType: String,
        val entityID: String
    )

    data class VoteResponse(
        val message: String
    )

}