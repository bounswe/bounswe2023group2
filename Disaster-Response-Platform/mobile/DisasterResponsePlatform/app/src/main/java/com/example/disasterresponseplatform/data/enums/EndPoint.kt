package com.example.disasterresponseplatform.data.enums

enum class Endpoint(val path: String) {
    LOGIN("users/login"),
    SIGNUP("users/signup"),
    DATA("joke/any"),
    USER("user"),
    PRODUCTS("products"),
    RESOURCE("resource"),
    NEED("need"),
    ME("users/me"),
    ME_OPTIONAL("profiles/all-user-optional-infos"),
    ME_SET("users/update-user"),
    ME_OPTIONAL_SET("profiles/user-optional-infos/add-user-optional-info"),
    LANGUAGE_GET("profiles/languages"),
    LANGUAGE_SET("profiles/languages/add-language"),
    LANGUAGE_DELETE("profiles/languages/delete-language"),
    SOCIAL_MEDIA_GET("profiles/socialmedia-links"),
    SOCIAL_MEDIA_SET("profiles/socialmedia-links/add-socialmedia-link"),
    SOCIAL_MEDIA_DELETE("profiles/socialmedia-links"),
    SKILL_GET("profiles/skills"),
    SKILL_SET("profiles/skills/add-skill"),
    SKILL_DELETE("profiles/skills"),
    PROFESSION_GET("profiles/professions"),
    PROFESSION_SET("profiles/professions/add-profession"),
    PROFESSION_DELETE("profiles/professions"),
}