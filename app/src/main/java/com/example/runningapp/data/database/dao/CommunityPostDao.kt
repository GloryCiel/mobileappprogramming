package com.example.runningapp.data.database.dao

import androidx.room.*
import com.example.runningapp.data.database.entities.CommunityPost

@Dao
interface CommunityPostDao {
    @Insert
    suspend fun insert(communityPost: CommunityPost): Long

    @Query("SELECT * FROM community_posts WHERE userId = :userId")
    suspend fun getCommunityPostsByUserId(userId: Int): List<CommunityPost>
}
