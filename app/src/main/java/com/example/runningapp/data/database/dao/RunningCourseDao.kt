package com.example.runningapp.data.database.dao

import androidx.room.*
import com.example.runningapp.data.database.entities.RunningCourse

@Dao
interface RunningCourseDao {
    @Insert
    suspend fun insert(runningCourse: RunningCourse): Long

    @Query("SELECT * FROM running_courses WHERE userId = :userId")
    suspend fun getCoursesByUserId(userId: Int): List<RunningCourse>
}
