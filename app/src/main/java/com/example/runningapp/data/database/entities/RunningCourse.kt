package com.example.runningapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_courses")
data class RunningCourse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val gpxFilePath: String,
    val title: String,
    val date: String,
    val distance: Double,
    val previewImagePath: String,
    val details: String
)
