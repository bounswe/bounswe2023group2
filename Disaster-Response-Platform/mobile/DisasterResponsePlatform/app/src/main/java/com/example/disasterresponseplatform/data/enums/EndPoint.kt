package com.example.disasterresponseplatform.data.enums

enum class Endpoint(val path: String) {
    DATA("joke/any"),
    USER("user"),
    PRODUCTS("products");
}