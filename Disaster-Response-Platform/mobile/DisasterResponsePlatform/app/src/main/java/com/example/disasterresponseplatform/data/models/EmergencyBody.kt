package com.example.disasterresponseplatform.data.models

import com.example.disasterresponseplatform.utils.StringUtil
import com.google.gson.annotations.SerializedName

class EmergencyBody {

    data class EmergencyResponse(val emergencies: List<EmergencyItem>)

    data class EmergencyItem(
        @SerializedName("created_by_user") val created_by_user: String? = null,
        @SerializedName("contact_name") val contact_name: String? = null,
        @SerializedName("contact_number") val contact_number: String?,

        @SerializedName("created_at") val created_at: String,
        @SerializedName("last_updated_at") val last_updated_at: String = created_at,

        @SerializedName("emergency_type") val type: String,
        @SerializedName("description") val description: String?,
        @SerializedName("upvote") val upvote: Int = 0,
        @SerializedName("downvote") val downvote: Int = 0,
        @SerializedName("reliability") val reliability: Double,

        @SerializedName("x") val x: Double = 0.0,
        @SerializedName("y") val y: Double = 0.0,
        @SerializedName("location") val location: String?,

        @SerializedName("active") val active: Boolean = true,
        @SerializedName("_id") val _id: String = StringUtil.generateRandomStringID()
    )

    data class EmergencyPostBody(
        val contact_name: String?,
        val contact_number: String?,
        val created_at: String,
        val emergency_type: String,
        val description: String?,
        val x: Double,
        val y: Double,
        val location: String?,
        val is_active: Boolean = true,
        )

    data class PostEmergencyResponseList(
        val emergencies: List<PostEmergencyResponseBody>
    )

    data class PostEmergencyResponseBody(
        @SerializedName("_id")
        val _id: String
    )
}