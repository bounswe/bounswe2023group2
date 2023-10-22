package com.example.disasterresponseplatform.data.enums

enum class Urgency(val type: Int) {
    EMERGENCY(0),
    CRITICAL(1),
    URGENT(2),
    HIGH_PRIORITY(3),
    NORMAL_PRIORITY(4),
    LOW_PRIORITY(5)
}