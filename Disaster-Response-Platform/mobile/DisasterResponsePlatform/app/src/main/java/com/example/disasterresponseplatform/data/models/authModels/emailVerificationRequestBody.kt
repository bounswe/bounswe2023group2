package com.example.disasterresponseplatform.data.models.authModels

import com.google.gson.annotations.SerializedName

data class EmailVerificationVerifyRequestBody(
    @SerializedName("token")
    val token: String,
)