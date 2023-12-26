package com.example.disasterresponseplatform.data.models

import com.google.gson.annotations.SerializedName

class ResourceBody {

    data class ResourceFormFieldsResponse(
        @SerializedName("fields") val fields: List<ResourceFormFields>,
    )
    data class ResourceFormFields(
        @SerializedName("name") val name: String,
        @SerializedName("label") val label: String,
        @SerializedName("type") val type: String,
        @SerializedName("options") val options: List<String>? = null
    )

    data class PostRegisterResponseList(
        val resources: List<PostRegisterResponseBody>
    )

    /**
     * This is for post Data
     */
    data class ResourceRequestBody(
        //@SerializedName("created_by") val created_by: String,
        @SerializedName("description") val description: String?,
        @SerializedName("initialQuantity") val initialQuantity: Int,
        @SerializedName("currentQuantity") val currentQuantity: Int,
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


    data class PostRegisterResponseBody(
        @SerializedName("_id")
        val _id: String
    )

    data class OtherFields(
        val fieldName: String,
        val input: String
    )

    data class DetailedFields(
        val fieldName: String,
        val input: String
    )

    data class ResourceResponse(val resources: List<ResourceItem>)

    enum class Condition(val displayName: String){
        new("New"),
        used("Used")
    }

    /**
     * Item that comes from get all resources
     */
    data class ResourceItem(
        @SerializedName("created_by") val created_by: String,
        @SerializedName("description") val description: String?,
        @SerializedName("initialQuantity") val initialQuantity: Int,
        @SerializedName("currentQuantity") val currentQuantity: Int,
        @SerializedName("type") val type: String,
        @SerializedName("details") val details: MutableMap<String,String>,
        @SerializedName("recurrence_rate") val recurrence_rate: Int?,
        @SerializedName("recurrence_deadline") val recurrence_deadline: String?,
        @SerializedName("x") val x: Double,
        @SerializedName("y") val y: Double,
        @SerializedName("occur_at") val occur_at: String?,
        @SerializedName("active") val active: Boolean,
        @SerializedName("created_at") val created_at: String,
        @SerializedName("last_updated_at") val last_updated_at: String,
        @SerializedName("upvote") val upvote: Int,
        @SerializedName("downvote") val downvote: Int,
        @SerializedName("_id") val _id: String,
        @SerializedName("open_address")val open_address : String?,
    )
    data class ResourceSearchResponse(val results: List<ResourceItem>)

}