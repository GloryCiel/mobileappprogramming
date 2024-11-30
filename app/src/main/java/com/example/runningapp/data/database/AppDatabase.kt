package com.example.runningapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.runningapp.data.database.dao.CommunityPostDao
import com.example.runningapp.data.database.dao.CrewPostDao
import com.example.runningapp.data.database.dao.RunningCourseDao
import com.example.runningapp.data.database.dao.UserDao
import com.example.runningapp.data.database.entities.CommunityPost
import com.example.runningapp.data.database.entities.CrewPost
import com.example.runningapp.data.database.entities.RunningCourse
import com.example.runningapp.data.database.entities.User

@Database(
    entities = [User::class, RunningCourse::class, CrewPost::class, CommunityPost::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun runningCourseDao(): RunningCourseDao
    abstract fun crewPostDao(): CrewPostDao
    abstract fun communityPostDao(): CommunityPostDao
}
