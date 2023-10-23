package com.example.disasterresponseplatform.data.models.usertypes

class CredibleUser(username: String, email: String, phone: String, name: String, surname: String,
                   var region: String ): AuthenticatedUser(username, email, phone, name, surname) {
    var createdNeeds: MutableList<Int> = mutableListOf()
}