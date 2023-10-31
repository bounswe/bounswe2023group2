package com.example.disasterresponseplatform.data.enums

enum class Endpoint(val path: String) {
    LOGIN("users/login"),
    SIGNUP("users/signup"),
    DATA("joke/any"),
    USER("user"),
    PRODUCTS("products"),
    RESOURCE("resource"),
    NEED("need");
}