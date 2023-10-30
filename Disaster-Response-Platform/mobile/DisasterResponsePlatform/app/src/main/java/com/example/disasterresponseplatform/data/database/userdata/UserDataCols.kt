package com.example.disasterresponseplatform.data.database.userdata

import java.util.UUID

class UserDataCols {

        companion object{
            const val id: String = "id"
            const val username: String = "username"
            const val email: String = "email"
            const val phone: String = "phone"
            const val name: String = "name"
            const val surname: String = "surname"
            const val isEmailVerified: String = "isEmailVerified"
            const val isPhoneVerified: String = "isPhoneVerified"
            const val notifications: String = "notifications"
            const val verificationLevel: String = "verificationLevel"
            const val profileInfoShared: String = "profileInfoShared"
            const val verifiedBy: String = "verifiedBy"
            const val birth: String = "birth"
            const val nationality: String = "nationality"
            const val idNumber: String = "idNumber"
            const val address: String = "address"
            const val education: String = "education"
            const val healthCondition: String = "healthCondition"
            const val bloodType: String = "bloodType"
            const val profilePhoto: String = "profilePhoto"
            const val socialMedia: String = "socialMedia"
            const val skills: String = "skills"
            const val languages: String = "languages"
            const val professions: String = "professions"
        }
}