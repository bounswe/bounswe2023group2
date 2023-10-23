package com.example.disasterresponseplatform.models

class RoleBasedUser(username: String, email: String, phone: String, name: String, surname: String,
                    var proficiency: String): AuthenticatedUser(username, email, phone, name, surname) {
    var createdInfo: MutableList<Int> = mutableListOf()
}