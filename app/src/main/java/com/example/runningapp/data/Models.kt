package com.example.runningapp.data

data class User(
    val id: Int,
    val password: String,
    val profilePhotoPath: String = "file:///android_asset/default/defaultProfile.png",
    val name: String,
    val rank: UserRank = UserRank.BRONZE,
    val preferredRegion: PreferredRegion,
    val createdAt: Long = System.currentTimeMillis()
)

data class Course(
    val id: Int,
    val title: String,
    val date: Long = System.currentTimeMillis(),
    val distance: Float,
    val courseImagePath: String = "file:///android_asset/default/defaultCourse.png",
    val description: String,
    val gpxFilePath: String,
    val userId: Int,
    val isPublic: Boolean = true
)

data class CrewPost(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val courseImagePath: String = "file:///android_asset/default/defaultCourse.png",
    val userId: Int,
    val courseId: Int?,
    val maxMembers: Int? = null,
    val currentMembers: Int = 1
)

data class CommunityPost(
    val id: Int,
    val tag: CommunityTag = CommunityTag.ALL,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val imagePath: String = "file:///android_asset/default/defaultPost.png",
    val userId: Int,
    val likeCount: Int = 0,
    val commentCount: Int = 0
)