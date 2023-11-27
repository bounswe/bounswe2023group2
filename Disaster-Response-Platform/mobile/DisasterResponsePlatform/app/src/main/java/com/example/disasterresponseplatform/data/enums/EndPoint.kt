package com.example.disasterresponseplatform.data.enums

enum class Endpoint(val path: String) {
    LOGIN("users/login"),
    SIGNUP("users/signup"),
    DATA("joke/any"),
    USER("user"),
    PRODUCTS("products"),
    RESOURCE("resources"),
    NEED("needs"),
    ME("users/me"),
    ME_OPTIONAL("profiles/user-optional-infos"),
    ME_OPTIONAL_DELETE("profiles/delete-user-optional-info"),
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
    FORM_FIELDS_RESOURCE("form_fields/resource"),
    FORM_FIELDS_NEED("form_fields/need"),
    FORM_FIELDS_TYPE("form_fields/type"),
}