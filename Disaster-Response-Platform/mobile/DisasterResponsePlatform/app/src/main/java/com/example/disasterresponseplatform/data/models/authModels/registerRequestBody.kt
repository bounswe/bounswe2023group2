package com.example.disasterresponseplatform.data.models.authModels

import com.google.gson.annotations.SerializedName
import java.io.Serial

data class RegisterRequestBody(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("disabled") val disabled: Boolean,
    @SerializedName("password") val password: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("is_email_verified") val isEmailVerified: Boolean,
    @SerializedName("private_account") val privateAccount: Boolean
)

data class SignUpResponseBody(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)

data class SignUpResponseBody400 (
    @SerializedName("ErrorMessage")
    val errorMessage :String,

    @SerializedName("ErrorDetail")
    val errorDetail :String,
)


data class SignUpResponseBody422(
    @SerializedName("detail")
    val detail: List<ValidationErrorDetail>
)

data class ValidationErrorDetail(
    @SerializedName("loc")
    val location: List<String>,
    @SerializedName("msg")
    val message: String,
    @SerializedName("type")
    val type: String
)

data class UsersMeResponse(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("is_email_verified")
    val isEmailVerified: Boolean,
    @SerializedName("private_account")
    val privateAccount: Boolean,
)

data class UserRolesResponse(
@SerializedName("user_role")
    val user_role: String,
)

data class UsersMeOptionalResponse(
    @SerializedName("username")
    val username: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("nationality")
    val nationality: String,
    @SerializedName("identity_number")
    val identityNumber: String,
    @SerializedName("education")
    val education: String,
    @SerializedName("health_condition")
    val healthCondition: String,
    @SerializedName("blood_type")
    val bloodType: String,
    @SerializedName("Address")
    val address: String,
    @SerializedName("profile_picture")
    val profilePicture: String,
)

data class UsersOptionalArray(
    @SerializedName("user_optional_infos")
    val list: List<UsersMeOptionalResponse>
)

data class ProfileBody(
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("private_account") val privateAccount: Boolean,
)

data class ProfileOptionalBody(
    @SerializedName("username") val username: String,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    @SerializedName("nationality") val nationality: String?,
    @SerializedName("identity_number") val identityNumber: String?,
    @SerializedName("education") val education: String?,
    @SerializedName("health_condition") val healthCondition: String?,
    @SerializedName("blood_type") val bloodType: String?,
    @SerializedName("Address") val address: String?,
    @SerializedName("profile_picture") val profilePicture: String?,
)

data class ProfficiencyRequest(
    @SerializedName("proficiency") val proficiency: String,
    @SerializedName("details") val details: String,
)

data class Skill(
    @SerializedName("username") val first: String,
    @SerializedName("skill_definition") val second: String,
    @SerializedName("skill_level") val third: String,
    @SerializedName("skill_document") val skillDocument: String?,
)

data class SkillArray(
    @SerializedName("user_skills") val list: MutableList<Skill>
)
data class Profession(
    @SerializedName("username") val first: String,
    @SerializedName("profession") val second: String,
    @SerializedName("profession_level") val third: String,
)

data class ProfessionArray(
    @SerializedName("user_professions") val list: MutableList<Profession>
)

data class SocialMediaLink(
    @SerializedName("username") val first: String,
    @SerializedName("platform_name") val second: String,
    @SerializedName("profile_URL") val third: String,
)

data class SocialMediaArray(
    @SerializedName("user_socialmedia_links") val list: MutableList<SocialMediaLink>
)

data class Language(
    @SerializedName("username") val first: String,
    @SerializedName("language") val second: String,
    @SerializedName("language_level") val third: String,
)

data class LanguageArray(
    @SerializedName("user_languages") val list: MutableList<Language>
)
