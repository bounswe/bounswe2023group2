package com.example.disasterresponseplatform.models.enums

enum class Endpoint(val path: String) {
    DATA("joke/any"),
    USER("user"),
    PRODUCTS("products");
}

enum class RequestType {
    GET, POST, PUT, DELETE // Add more HTTP methods as needed
}