package com.example.runningapp.repository

import com.example.runningapp.data.database.dao.CrewPostDao
import com.example.runningapp.data.database.entities.CrewPost

class CrewRepository(private val crewPostDao: CrewPostDao) {

    // 크루 글 삽입
    suspend fun addCrewPost(post: CrewPost): Long {
        return crewPostDao.insert(post)
    }

    // 특정 사용자 ID의 모든 크루 글 가져오기
    suspend fun getCrewPostsByUserId(userId: Int): List<CrewPost> {
        return crewPostDao.getCrewPostsByUserId(userId)
    }
}
