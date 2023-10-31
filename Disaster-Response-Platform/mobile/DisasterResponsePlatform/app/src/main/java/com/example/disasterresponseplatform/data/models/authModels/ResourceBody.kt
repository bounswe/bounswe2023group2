package com.example.disasterresponseplatform.data.models.authModels

import com.google.gson.annotations.SerializedName

class ResourceBody {

    data class ResourceRequestBody(
        @SerializedName("created_by") val created_by: String,
        @SerializedName("condition") val condition: String,
        @SerializedName("initialQuantity") val initialQuantity: Int,
        @SerializedName("currentQuantity") val currentQuantity: Int,
        @SerializedName("type") val type: String,
        @SerializedName("details") val details: String ,
        @SerializedName("x") val x: Float,
        @SerializedName("y") val y: Float
    )

    data class ResourceDetails(val size: String?, val gender: String?, val age: Any?,
                               val subtype: String?, val expiration_date: String?, val allergens: String?,
                               val tool_type: String?, val estimated_weight: Int?, val disease_name: String?,
                               val medicine_name: String?, val start_location: String?,
                               val end_location: String?, val number_of_people: Int?, val weather_condition: String?, val SKT: String?)


    data class ResourceResponse(val resources: List<ResourceItem>)

    data class ResourceItem(
        val created_by: String,
        val condition: String,
        val initialQuantity: Int,
        val currentQuantity: Int,
        val type: String,
        val details: ResourceDetails,
        val x: Float?,
        val y: Float?,
        val _id: String
    )

}