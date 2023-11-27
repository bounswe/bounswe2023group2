package com.example.disasterresponseplatform.data.models

import com.google.gson.annotations.SerializedName

class ActionBody {

    data class ActionsResponse(
        @SerializedName("actions") val actions: List<ActionItem>
    )

    data class ActionItem(
        @SerializedName("created_by") val createdBy: String,
        @SerializedName("description") val description: String,
        @SerializedName("type") val type: String,
        @SerializedName("start_location_x") val startLocationX: Double,
        @SerializedName("start_location_y") val startLocationY: Double,
        @SerializedName("endLocation_x") val endLocationX: Double,
        @SerializedName("endLocation_y") val endLocationY: Double,
        @SerializedName("status") val status: String,
        @SerializedName("occur_at") val occurAt: String,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("last_updated_at") val lastUpdatedAt: String,
        @SerializedName("upvote") val upvote: Int,
        @SerializedName("downvote") val downvote: Int,
        @SerializedName("related_groups") val relatedGroups: List<RelatedGroup>,
        @SerializedName("end_at") val endAt: String,
        @SerializedName("_id") val id: String
    )

    data class RelatedGroup(
        @SerializedName("recurrence") val recurrence: Boolean,
        @SerializedName("group_type") val groupType: String,
        @SerializedName("related_needs") val relatedNeeds: List<String>,
        @SerializedName("related_resources") val relatedResources: List<String>
    )
}