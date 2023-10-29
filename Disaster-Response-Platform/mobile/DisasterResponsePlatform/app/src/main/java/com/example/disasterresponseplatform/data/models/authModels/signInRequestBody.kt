package com.example.disasterresponseplatform.data.models.authModels

import com.google.gson.annotations.SerializedName

data class SignInRequestBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("disabled")
    val disabled: Boolean,
)

data class SignInResponseBody(

    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)