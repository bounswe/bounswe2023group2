package com.example.disasterresponseplatform.data.models

import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.google.gson.annotations.SerializedName

class NeedBody {

    data class NeedRequestBody(
        @SerializedName("created_by") val created_by: String,
        @SerializedName("initialQuantity") val initialQuantity: Int,
        @SerializedName("urgency") val urgency: Int,
        @SerializedName("unsuppliedQuantity") val unsuppliedQuantity: Int,
        @SerializedName("type") val type: String,
        @SerializedName("details") val details: Details,
        @SerializedName("x") val x: Double?,
        @SerializedName("y") val y: Double?
    )


    data class PostNeedResponseList(
        val needs: List<PostNeedResponseBody>
    )
    data class PostNeedResponseBody(
        @SerializedName("_id")
        val _id: String
    )

    data class Details(
        val subtype: String?
    )

    data class NeedDetails(val size: String?, val gender: String?, val age: Any?,
                               val subtype: String?, val expiration_date: String?, val allergens: String?,
                               val tool_type: String?, val estimated_weight: Int?, val disease_name: String?,
                               val medicine_name: String?, val start_location: String?,
                               val end_location: String?, val number_of_people: Int?, val weather_condition: String?, val SKT: String?)


    data class NeedResponse(val needs: List<NeedItem>)

    data class NeedItem(
        val created_by: String,
        val initialQuantity: Int,
        val urgency: Int,
        val unSuppliedQuantity: Int,
        val type: String,
        val details: NeedDetails,
        val x: Float?,
        val y: Float?,
        val _id: String
    ) {
        fun returnNeedType(): NeedTypes {
            val needType: NeedTypes = when (type.lowercase()) {
                "cloth" -> NeedTypes.Clothes
                "clothes" -> NeedTypes.Clothes
                "food" -> NeedTypes.Food
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
            return "Need: ${needType.name}, Urgency: $urgency, Quantity: $initialQuantity"
        }

        fun getSubDescription(): String {
            return returnDetailsAsString()
        }
    }


}