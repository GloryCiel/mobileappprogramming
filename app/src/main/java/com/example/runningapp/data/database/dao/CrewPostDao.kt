package com.example.runningapp.data.database.dao

import androidx.room.*
import com.example.runningapp.data.database.entities.CrewPost

@Dao
interface CrewPostDao {
    @Insert
    suspend fun insert(crewPost: CrewPost): Long

    @Query("SELECT * FROM crew_posts WHERE userId = :userId")
    suspend fun getCrewPostsByUserId(userId: Int): List<CrewPost>
}
