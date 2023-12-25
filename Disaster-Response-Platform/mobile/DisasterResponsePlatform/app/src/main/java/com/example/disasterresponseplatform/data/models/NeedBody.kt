package com.example.disasterresponseplatform.data.models

import com.google.gson.annotations.SerializedName

class NeedBody {

    data class NeedFormFieldsResponse(
        @SerializedName("fields") val fields: List<NeedFormFields>,
    )

    data class NeedFormFields(
        @SerializedName("name") val name: String,
        @SerializedName("label") val label: String,
        @SerializedName("type") val type: String,
        @SerializedName("options") val options: List<String>? = null
    )

    data class NeedRequestBody(
        //@SerializedName("created_by") val created_by: String,
        @SerializedName("description") val description: String?,
        @SerializedName("initialQuantity") val initialQuantity: Int,
        @SerializedName("urgency") val urgency: Int,
        @SerializedName("unsuppliedQuantity") val unsuppliedQuantity: Int,
        @SerializedName("type") val type: String,
        @SerializedName("details") val details: MutableMap<String,String>,
        @SerializedName("open_address")val open_address : String?,
        @SerializedName("x") val x: Double,
        @SerializedName("y") val y: Double,
        @SerializedName("occur_at") val occur_at: String?,
        @SerializedName("recurrence_rate") val recurrence_rate: Int?,
        @SerializedName("recurrence_deadline") val recurrence_deadline: String?,
        @SerializedName("active") val active: Boolean = true,
        @SerializedName("upvote") val upvote: Int = 0,
        @SerializedName("downvote") val downvote: Int = 0,
    )

    data class OtherFields(
        val fieldName: String,
        val input: String
    )

    data class DetailedFields(
        val fieldName: String,
        val input: String
    )

    data class PostNeedResponseList(
        val needs: List<PostNeedResponseBody>
    )
    data class PostNeedResponseBody(
        @SerializedName("_id")
        val _id: String
    )

    data class NeedResponse(val needs: List<NeedItem>)

    /**
     * Item that comes from get all needs
     */
    data class NeedItem(
        @SerializedName("created_by") val created_by: String,
        @SerializedName("description") val description: String?,
        @SerializedName("initialQuantity") val initialQuantity: Int,
        @SerializedName("urgency") val urgency: Int,
        @SerializedName("unsuppliedQuantity") val unsuppliedQuantity: Int,
        @SerializedName("type") val type: String,
        @SerializedName("details") val details: MutableMap<String,String>,
        @SerializedName("open_address")val open_address : String?,
        @SerializedName("x") val x: Double,
        @SerializedName("y") val y: Double,
        @SerializedName("occur_at") val occur_at: String?,
        @SerializedName("recurrence_rate") val recurrence_rate: Int?,
        @SerializedName("recurrence_deadline") val recurrence_deadline: String?,
        @SerializedName("created_at") val created_at: String,
        @SerializedName("last_updated_at") val last_updated_at: String,
        @SerializedName("active") val active: Boolean,
        @SerializedName("upvote") val upvote: Int,
        @SerializedName("downvote") val downvote: Int,
        @SerializedName("_id") val _id: String
    )

    data class NeedSearchResponse(val results: List<NeedItem>)
}