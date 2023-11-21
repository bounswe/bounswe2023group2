package com.example.disasterresponseplatform.data.models.usertypes

import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

open class AuthenticatedUser(var username: String, var email: String, var phone: String,
                             var name: String, var surname: String ) : Serializable {
    var id: UUID = UUID.randomUUID()
    var isEmailVerified: Boolean = false
    var isPhoneVerified: Boolean = false
    // TODO: Topic class is not implemented yet
//    var notifications: MutableList<Topic> = mutableListOf()
    var verificationLevel: Int = 0
    var profileInfoShared: Boolean = false
    var verifiedBy: String = ""
    var birth: String? = null
    var nationality: String? = null
    var idNumber: String? = null
    var address: String? = null
    var education: String? = null
    var healthCondition: String? = null
    var bloodType: String? = null
    var profilePhoto: String? = null
    var socialMedia: MutableList<SocialMedia> = mutableListOf()
    var skills: MutableList<Skill> = mutableListOf()
    var languages: MutableList<Language> = mutableListOf()
    var professions: MutableList<Profession> = mutableListOf()

    class SocialMedia(var platformName: String, var profileURL: String)
    class Skill(var definition: String, var level: String, var document: String)
    class Language(var language: String, var level: String)
    class Profession(var profession: String, var level: String)

}