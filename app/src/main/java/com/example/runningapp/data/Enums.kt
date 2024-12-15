package com.example.runningapp.data

enum class CommunityTag(val korName: String) {
    ALL("전체"),
    TAG1("공지사항"),
    TAG2("질문"),
    TAG3("정보공유"),
    TAG4("태그1"),
    TAG5("태그2"),
    TAG6("태그3");

    override fun toString(): String = korName
}

enum class MyPageTag(val korName: String) {
    CREW("크루"),
    COMMUNITY("커뮤니티");

    override fun toString(): String = korName
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