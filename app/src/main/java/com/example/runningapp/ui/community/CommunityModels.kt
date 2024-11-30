package com.example.runningapp.ui.community

// 데이터 클래스 추가
data class CommunityItem(
    val tag: String,
    val title: String,
    val content: String,
    val userImage: Int,
    val userName: String,
    val userRank: String
)

object CommunityTags {
    val tags = listOf(
        "All",
        "tag1",
        "tag2",
        "tag3",
        "tag4",
        "tag5"
    )
}