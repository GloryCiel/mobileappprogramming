package com.example.runningapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(defaultValue = "file:///android_asset/defaultProfile.png") val profilePhotoPath: String,
    val name: String,
    val rank: String,
    val preferredRegion: String
)
