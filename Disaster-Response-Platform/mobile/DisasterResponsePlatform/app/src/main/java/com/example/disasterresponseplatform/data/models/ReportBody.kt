package com.example.disasterresponseplatform.data.models

import com.google.gson.annotations.SerializedName

class ReportBody {

    data class ReportRequestBody(
        @SerializedName("created_by") val createdBy: String,
        @SerializedName("description") val description: String,
        @SerializedName("report_type") val reportType: String,
        @SerializedName("report_type_id") val reportTypeId: String,
        @SerializedName("details") val details: Map<String, Any>,
        @SerializedName("status") val status: String = "undefined"
    )

}