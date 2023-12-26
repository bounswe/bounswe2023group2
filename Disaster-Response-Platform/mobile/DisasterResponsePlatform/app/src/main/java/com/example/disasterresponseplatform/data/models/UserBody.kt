package com.example.disasterresponseplatform.data.models

class UserBody {

    data class responseBody(
        val user_role: String
    )

    data class UserMessageModel(
        val username: String?,
        val is_credible: Boolean
    )

}