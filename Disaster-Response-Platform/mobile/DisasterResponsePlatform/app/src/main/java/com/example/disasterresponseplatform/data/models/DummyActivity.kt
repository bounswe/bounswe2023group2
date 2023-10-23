package com.example.disasterresponseplatform.data.models

import com.example.disasterresponseplatform.data.enums.ActivityEnum
import com.example.disasterresponseplatform.data.enums.PredefinedTypes

class DummyActivity(var activityType: ActivityEnum, var predefinedTypes: PredefinedTypes, var location: String,
                    var date: String, var reliabilityScale: Double) {
}