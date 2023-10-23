package com.example.disasterresponseplatform.models

class CredibleUser(username: String, email: String, phone: String, name: String, surname: String,
                   var region: String ): AuthenticatedUser(username, email, phone, name, surname) {
    var createdNeeds: MutableList<Int> = mutableListOf()
}