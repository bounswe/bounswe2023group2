package com.example.disasterresponseplatform.data.models

class RecurrenceModel {

    data class RecurrenceResponse(val events: List<GetRecurrenceBody>)

    data class PostRecurrenceBody(
        val title: String, // unit + start + end day
        val description: String, // description of activity
        val activity: String,
        val occurance_rate: Int, // 1,2,3 ?
        val occurance_unit: String, // day, week, month
        val duration: Int? = 1, // end - start day
        val start_at: String, // YYYY-MM-DD
        val end_at: String, // YYYY-MM-DD
    )

    data class GetRecurrenceBody(
        val _id: String,
        val created_by: String,
        val title: String, // unit + start + end day
        val description: String, // description of activity
        val activity: String,
        val status: String, // created, stopped vs
        val occurance_rate: Int, // 1,2,3 ?
        val occurance_unit: String, // day, week, month
        val occurring_dates: List<String>?, // null in general
        val duration: Int? = 1, // end - start day
        val recurring_items: List<String>?, // id of recurring items
        val created_at: String, // YYYY-MM-DD HH:MM:SS
        val last_updated_at: String, // YYYY-MM-DD HH:MM:SS
        val start_at: String, // YYYY-MM-DD
        val end_at: String, // YYYY-MM-DD
    )

    data class PostRecurrenceResponseBody(
        val message: String,
        val recurrence_id: String
    )


    data class AttachActivityBody(
        val recurrence_id: String,
        val activity_id: String,
        val activity_type: String // need,resource,action,event (with lower case letter)
    )

    data class GetRecurrenceWithIDResponseBody(
        val payload: GetRecurrenceBody,
        val message: String,
    )


}