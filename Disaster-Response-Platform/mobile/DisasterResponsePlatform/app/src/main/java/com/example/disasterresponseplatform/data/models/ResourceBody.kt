package com.example.disasterresponseplatform.data.models

import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.google.gson.annotations.SerializedName

class ResourceBody {

    data class ResourceRequestBody(
        @SerializedName("created_by") val created_by: String,
        @SerializedName("condition") val condition: String,
        @SerializedName("initialQuantity") val initialQuantity: Int,
        @SerializedName("currentQuantity") val currentQuantity: Int,
        @SerializedName("type") val type: String,
        @SerializedName("details") val details: Details,
        @SerializedName("x") val x: Double?,
        @SerializedName("y") val y: Double?
    )


    data class PostRegisterResponseList(
        val resources: List<PostRegisterResponseBody>
    )
    data class PostRegisterResponseBody(
        @SerializedName("_id")
        val _id: String
    )

    data class Details(
        val subtype: String?
    )

    data class ResourceDetails(val size: String?, val gender: String?, val age: Any?,
                               val subtype: String?, val expiration_date: String?, val allergens: String?,
                               val tool_type: String?, val estimated_weight: Int?, val disease_name: String?,
                               val medicine_name: String?, val start_location: String?,
                               val end_location: String?, val number_of_people: Int?, val weather_condition: String?, val SKT: String?)


    data class ResourceResponse(val resources: List<ResourceItem>)

    data class ResourceItem(
        val created_by: String,
        val condition: String,
        val initialQuantity: Int,
        val currentQuantity: Int,
        val type: String,
        val details: ResourceDetails,
        val x: Float?,
        val y: Float?,
        val _id: String
    ) {
        fun returnNeedType(): NeedTypes {
            val needType: NeedTypes = when (type.lowercase()) {
                "cloth" -> NeedTypes.Cloth
                "food" -> NeedTypes.Food
                "drink" -> NeedTypes.Drink
                "shelter" -> NeedTypes.Shelter
                "medication" -> NeedTypes.Medication
                "transportation" -> NeedTypes.Transportation
                "tools" -> NeedTypes.Tools
                "human" -> NeedTypes.Human
                else -> NeedTypes.Other
            }
            return needType
        }

        fun returnDetailsAsString(): String {
            var detailsString = ""
            if (!details.size.isNullOrEmpty()) {
                detailsString += "size: ${details.size} "
            }
            if (!details.gender.isNullOrEmpty()) {
                detailsString += "gender: ${details.gender} "
            }
            if (details.age != null) {
                detailsString += "age: ${details.age} "
            }
            if (!details.subtype.isNullOrEmpty()) {
                detailsString += "subtype: ${details.subtype} "
            }
            if (!details.expiration_date.isNullOrEmpty()) {
                detailsString += "expiration_date: ${details.expiration_date} "
            }
            if (!details.allergens.isNullOrEmpty()) {
                detailsString += "allergens: ${details.allergens} "
            }
            if (!details.disease_name.isNullOrEmpty()) {
                detailsString += "disease_name: ${details.disease_name} "
            }
            if (!details.medicine_name.isNullOrEmpty()) {
                detailsString += "medicine_name: ${details.medicine_name} "
            }
            if (!details.start_location.isNullOrEmpty()) {
                detailsString += "start_location: ${details.start_location} "
            }
            if (!details.end_location.isNullOrEmpty()) {
                detailsString += "end_location: ${details.end_location} "
            }
            if (details.number_of_people != null) {
                detailsString += "number_of_people: ${details.number_of_people} "
            }
            if (!details.weather_condition.isNullOrEmpty()) {
                detailsString += "weather_condition: ${details.weather_condition} "
            }
            if (!details.SKT.isNullOrEmpty()) {
                detailsString += "SKT: ${details.SKT} "
            }
            return detailsString
        }
        fun getDescription(): String {
            val needType = returnNeedType()
            return "Resource: ${needType.name}, Condition: $condition, Quantity: $initialQuantity"
        }

        fun getSubDescription(): String {
            return returnDetailsAsString()
        }

        fun getResource(): Resource {
            return Resource(
                _id,
                created_by,
                condition,
                initialQuantity,
                returnNeedType(),
                returnDetailsAsString(),
                null,
                x!!.toDouble(),
                y!!.toDouble()
            )
        }
    }

}