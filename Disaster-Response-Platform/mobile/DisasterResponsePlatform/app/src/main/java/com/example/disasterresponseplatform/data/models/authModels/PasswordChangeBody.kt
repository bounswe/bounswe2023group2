package com.example.disasterresponseplatform.data.models.authModels

import com.google.gson.annotations.SerializedName

data class PasswordChangeSend(
    @SerializedName("new_password")
    val newPassword: String
)