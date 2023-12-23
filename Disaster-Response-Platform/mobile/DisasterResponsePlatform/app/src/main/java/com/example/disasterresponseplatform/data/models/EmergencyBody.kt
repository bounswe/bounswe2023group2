package com.example.disasterresponseplatform.data.models

import com.example.disasterresponseplatform.utils.StringUtil
import com.google.gson.annotations.SerializedName

class EmergencyBody {

    data class EmergencyResponse(val emergencies: List<EmergencyItem>)

    data class EmergencyItem(
        @SerializedName("created_by") val created_by: String? = null,
        @SerializedName("creator_name") val creator_name: String? = null,
        @SerializedName("phone_number") val phone_number: String?,

        @SerializedName("created_at") val created_at: String,
        @SerializedName("last_updated_at") val last_updated_at: String = created_at,

        @SerializedName("type") val type: String,
        @SerializedName("description") val description: String?,
        @SerializedName("upvote") val upvote: Int = 0,
        @SerializedName("downvote") val downvote: Int = 0,

        @SerializedName("x") val x: Double = 0.0,
        @SerializedName("y") val y: Double = 0.0,
        @SerializedName("location") val location: String?,

        @SerializedName("active") val active: Boolean = true,
        @SerializedName("_id") val _id: String = StringUtil.generateRandomStringID()
    )

}