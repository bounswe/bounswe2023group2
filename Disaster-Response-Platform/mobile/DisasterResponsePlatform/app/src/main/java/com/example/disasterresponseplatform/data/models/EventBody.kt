package com.example.disasterresponseplatform.data.models

import com.google.gson.annotations.SerializedName

class EventBody {

    data class EventResponse(val events: List<EventRequestBody>)

    data class EventRequestBody(
        val event_type: String,
        val event_time: String,
        val end_time: String?,
        val is_active: Boolean = true,
        val center_location_x: Double,
        val center_location_y: Double,
        val max_distance_x: Double?, // the radius to the center (coverage area)
        val max_distance_y: Double?,
        val created_time: String,
        val created_by_user: String,
        val last_confirmed_time: String?,
        val confirmed_by_user: String?,
        val upvote: Int,
        val downvote: Int,
        val short_description: String?,
        val note: String?,
        val _id: String
    )

    data class EventPostBody(
        val event_type: String,
        val event_time: String?,
        val is_active: Boolean = true,
        val center_location_x: Double,
        val center_location_y: Double,
        val max_distance_x: Double?, // the radius to the center (coverage area)
        val max_distance_y: Double?,
        val created_time: String?,
        val short_description: String,
        val note: String?
    )

    data class PostEventResponseList(
        val events: List<PostEventResponseBody>
    )

    data class PostEventResponseBody(
        @SerializedName("_id")
        val _id: String
    )

    /**
     * This enum class will be used to select items from spinner
     */
    enum class EventTypes(val displayName: String){
        debris("Debris"),
        infrastructure("Infrastructure"),
        disaster("Disaster"),
        helpArrived("Help-Arrived")
    }


}