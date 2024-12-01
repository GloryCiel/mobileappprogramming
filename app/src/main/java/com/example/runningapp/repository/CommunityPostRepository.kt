package com.example.runningapp.repository

import com.example.runningapp.data.database.dao.CommunityPostDao
import com.example.runningapp.data.database.entities.CommunityPost

class CommunityRepository(private val communityPostDao: CommunityPostDao) {

    // 커뮤니티 글 삽입
    suspend fun addCommunityPost(post: CommunityPost): Long {
        return communityPostDao.insert(post)
    }

    // 특정 사용자 ID의 모든 커뮤니티 글 가져오기
    suspend fun getCommunityPostsByUserId(userId: Int): List<CommunityPost> {
        return communityPostDao.getCommunityPostsByUserId(userId)
    }
}
