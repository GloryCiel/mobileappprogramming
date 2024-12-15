package com.example.runningapp.ui.crew

data class FindCrewItem(
    val title: String,
    val date: String,
    val location: String,
    val content: String,
    val userImage: Int,
    val userName: String,
    val userRank: String
)

object FindCrewTags {
    val locations = listOf(
        "중구",
        "서구",
        "남구",
        "동구",
        "북구",
        "수성구",
        "달서구"
    )
}