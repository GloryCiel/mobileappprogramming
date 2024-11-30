package com.example.runningapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crew_posts")
data class CrewPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val content: String,
    val createdAt: String,
    val imagePath: String,
    val region: String
)
