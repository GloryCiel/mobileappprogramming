package com.example.runningapp.repository

import com.example.runningapp.data.database.dao.RunningCourseDao
import com.example.runningapp.data.database.entities.RunningCourse

class RunningCourseRepository(private val runningCourseDao: RunningCourseDao) {

    // 러닝 코스 삽입
    suspend fun addRunningCourse(course: RunningCourse): Long {
        return runningCourseDao.insert(course)
    }

    // 특정 사용자 ID의 모든 러닝 코스 가져오기
    suspend fun getCoursesByUserId(userId: Int): List<RunningCourse> {
        return runningCourseDao.getCoursesByUserId(userId)
    }
}
