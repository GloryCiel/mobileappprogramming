package com.example.runningapp.data.database.dao

import androidx.room.*
import com.example.runningapp.data.database.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User
}
