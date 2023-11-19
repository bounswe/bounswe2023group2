package com.example.disasterresponseplatform.data.models

import com.google.gson.annotations.SerializedName

class NeedBody {

    data class NeedRequestBody(
        @SerializedName("created_by") val created_by: String,
        @SerializedName("initialQuantity") val initialQuantity: Int,
        @SerializedName("urgency") val urgency: Int,
        @SerializedName("unsuppliedQuantity") val unsuppliedQuantity: Int,
        @SerializedName("type") val type: String,
        @SerializedName("details") val details: Details,
        @SerializedName("x") val x: Double?,
        @SerializedName("y") val y: Double?
    )


    data class PostNeedResponseList(
        val needs: List<PostNeedResponseBody>
    )
    data class PostNeedResponseBody(
        @SerializedName("_id")
        val _id: String
    )

    data class Details(
        val subtype: String?
    )

    data class NeedDetails(val size: String?, val gender: String?, val age: Any?,
                               val subtype: String?, val expiration_date: String?, val allergens: String?,
                               val tool_type: String?, val estimated_weight: Int?, val disease_name: String?,
                               val medicine_name: String?, val start_location: String?,
                               val end_location: String?, val number_of_people: Int?, val weather_condition: String?, val SKT: String?)


    data class NeedResponse(val needs: List<NeedItem>)

    data class NeedItem(
        val created_by: String,
        val initialQuantity: Int,
        val urgency: Int,
        val unSuppliedQuantity: Int,
        val type: String,
        val details: NeedDetails,
        val x: Float?,
        val y: Float?,
        val _id: String
    )


}