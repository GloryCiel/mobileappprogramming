package com.example.runningapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_posts")
data class CommunityPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val tag: String,
    val title: String,
    val content: String
)
