package com.example.disasterresponseplatform.data.models.authModels

import com.google.gson.annotations.SerializedName

data class SignInRequestBody(
    @SerializedName("username_or_email_or_phone")
    val username: String,
    @SerializedName("password")
    val password: String
)

data class SignInResponseBody200(

    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)

data class SignInResponseBody422(
    @SerializedName("detail")
    val detail: List<ValidationErrorDetail>
)


data class SignInResponseBody401 (
    @SerializedName("ErrorMessage")
    val errorMessage :String,

    @SerializedName("ErrorDetail")
    val errorDetail :String,
)


