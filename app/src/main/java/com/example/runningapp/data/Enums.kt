package com.example.runningapp.data

enum class CommunityTag(val korName: String) {
    ALL("전체"),
    TAG1("공지사항"),
    TAG2("질문"),
    TAG3("정보공유"),
    TAG4("코스추천"),
    TAG5("오운완"),
    TAG6("자랑글");

    override fun toString(): String = korName

    companion object {
        fun fromKorName(korName: String): CommunityTag? {
            return values().find { it.korName == korName }
        }
    }
}

enum class MyPageTag(val korName: String) {
    RUNNING("러닝 코스"),
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