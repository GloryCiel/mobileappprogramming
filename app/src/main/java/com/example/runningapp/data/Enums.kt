package com.example.runningapp.data

enum class CommunityTag {
    ALL, TAG1, TAG2, TAG3, TAG4, TAG5
}

enum class UserRank {
    DIAMOND, PLATINUM, GOLD, SILVER, BRONZE
}

enum class PreferredRegion(val korName: String) {
    JUNG_GU("중구"),
    DONG_GU("동구"),
    SEO_GU("서구"),
    NAM_GU("남구"),
    BUK_GU("북구"),
    SUSEONG_GU("수성구"),
    DALSEO_GU("달서구"),
    DALSEONG_GUN("달성군");

    override fun toString(): String = korName
}