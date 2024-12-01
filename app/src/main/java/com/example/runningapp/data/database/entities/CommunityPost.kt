package com.example.runningapp.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "community_posts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // 부모 삭제 시 자식도 삭제
        )
    ],
    indices = [Index(value = ["userId"])] // 검색 최적화를 위해 Index 추가
)
data class CommunityPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val tag: String,
    val title: String,
    val content: String
)
