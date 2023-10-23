package com.example.disasterresponseplatform.data.enums

enum class Endpoint(val path: String) {
    LOGIN("api/login"),
    DATA("joke/any"),
    USER("user"),
    PRODUCTS("products");
}