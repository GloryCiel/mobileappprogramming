package com.example.runningapp.repository

import com.example.runningapp.data.database.dao.UserDao
import com.example.runningapp.data.database.entities.User

class UserRepository(private val userDao: UserDao) {

    // 사용자 삽입
    suspend fun addUser(user: User): Long {
        return userDao.insert(user)
    }

    // ID로 사용자 가져오기
    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }
}
