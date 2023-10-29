package com.example.disasterresponseplatform.data.models.authModels

import com.google.gson.annotations.SerializedName

data class RegisterRequestBody(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("disabled") val disabled: Boolean,
    @SerializedName("password") val password: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("is_email_verified") val isEmailVerified: Boolean,
    @SerializedName("private_account") val privateAccount: Boolean
)

data class SignUpResponseBody(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)

data class ValidationErrorResponse(
    @SerializedName("detail")
    val detail: List<ValidationErrorDetail>
)

data class ValidationErrorDetail(
    @SerializedName("loc")
    val location: List<String>,
    @SerializedName("msg")
    val message: String,
    @SerializedName("type")
    val type: String
)
