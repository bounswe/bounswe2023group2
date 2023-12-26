package com.example.disasterresponseplatform.data.models

import com.google.gson.annotations.SerializedName

class ActionBody {

    data class ActionFormFieldsResponse(
        @SerializedName("fields") val fields: List<ActionBody.ActionFormFields>,
    )

    data class ActionFormFields(
        @SerializedName("name") val name: String,
        @SerializedName("label") val label: String,
        @SerializedName("type") val type: String,
        @SerializedName("options") val options: List<String>? = null
    )


    data class ActionRequestBody(
        //@SerializedName("created_by") val createdBy: String,
        @SerializedName("description") val description: String?,
        @SerializedName("type") val type: String,
        //@SerializedName("start_location_x") val startLocationX: Double?,
        //@SerializedName("start_location_y") val startLocationY: Double?,
        //@SerializedName("endLocation_x") val endLocationX: Double?,
        //@SerializedName("endLocation_y") val endLocationY: Double?,
        //@SerializedName("status") val status: String,
        @SerializedName("occur_at") val occurAt: String?,
        @SerializedName("end_at") val endAt: String?,
        //@SerializedName("created_at") val createdAt: String,
        //@SerializedName("last_updated_at") val lastUpdatedAt: String,
        //@SerializedName("upvote") val upvote: Int,
        //@SerializedName("downvote") val downvote: Int,
        @SerializedName("related_groups") val relatedGroups: List<RelatedGroup>,
    )

    data class PostActionResponseList(
        val actions: List<PostActionResponseBody>
    )
    data class PostActionResponseBody(
        @SerializedName("_id")
        val _id: String
    )

    data class ActionResponse(val actions: List<ActionItem>)


    data class ActionItem(
        @SerializedName("created_by") val created_by: String,
        @SerializedName("description") val description: String?,
        @SerializedName("type") val type: String,
        //@SerializedName("start_location_x") val startLocationX: Double?,
        //@SerializedName("start_location_y") val startLocationY: Double?,
        //@SerializedName("endLocation_x") val endLocationX: Double?,
        //@SerializedName("endLocation_y") val endLocationY: Double?,
        //@SerializedName("status") val status: String,
        @SerializedName("occur_at") val occur_at: String?,
        @SerializedName("end_at") val end_at: String?,
        @SerializedName("created_at") val created_at: String,
        @SerializedName("last_updated_at") val last_updated_at: String,
        @SerializedName("upvote") val upvote: Int,
        @SerializedName("downvote") val downvote: Int,
        @SerializedName("reliability") val reliability: Double,
        @SerializedName("related_groups") val relatedGroups: List<RelatedGroup>,
        //@SerilizedName("related_groups") val related_groups: ??
        @SerializedName("_id") val _id: String
    )

    data class RelatedGroup(
        @SerializedName("recurrence") val recurrence: Boolean,
        @SerializedName("group_type") val groupType: String,
        @SerializedName("related_needs") val relatedNeeds: List<String>,
        @SerializedName("related_resources") val relatedResources: List<String>
    )

    data class ActionSearchResponse(val results: List<ActionItem>)
}