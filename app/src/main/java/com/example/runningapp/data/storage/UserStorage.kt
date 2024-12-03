package com.example.runningapp.data.storage

import android.content.Context
import com.example.runningapp.data.User
import com.example.runningapp.data.UserRank
import com.example.runningapp.data.PreferredRegion

object UserStorage : BaseStorage() {
    private const val FILENAME = "users.json"

    // 기본적인 CRUD 작업
    fun saveUsers(context: Context, users: List<User>) {
        saveToFile(context, FILENAME, users)
    }

    fun loadUsers(context: Context): List<User> {
        return loadFromFile(context, FILENAME) ?: emptyList()
    }

    fun getUserById(context: Context, userId: Int): User? {
        return loadUsers(context).find { it.id == userId }
    }

    fun addUser(
        context: Context,
        name: String,
        password: String,
        region: PreferredRegion
    ): User? {
        // 이미 존재하는 사용자인지 확인
        val users = loadUsers(context)
        if (users.any { it.name == name }) {
            return null
        }

        val newUser = User(
            id = getNextId(context, FILENAME),
            name = name,
            password = password,
            preferredRegion = region
        )

        val updatedUsers = users + newUser
        saveUsers(context, updatedUsers)
        return newUser
    }

    // 필터링 함수들
    fun getUsersByRank(context: Context, rank: UserRank): List<User> {
        return loadUsers(context).filter { it.rank == rank }
    }

    fun getUsersByRegion(context: Context, region: PreferredRegion): List<User> {
        return loadUsers(context).filter { it.preferredRegion == region }
    }

    // 업데이트 함수들
    fun updateUserRank(context: Context, userId: Int, newRank: UserRank): Boolean {
        val users = loadUsers(context).toMutableList()
        val index = users.indexOfFirst { it.id == userId }
        if (index == -1) return false

        users[index] = users[index].copy(rank = newRank)
        saveUsers(context, users)
        return true
    }

    fun updateUserRegion(context: Context, userId: Int, newRegion: PreferredRegion): Boolean {
        val users = loadUsers(context).toMutableList()
        val index = users.indexOfFirst { it.id == userId }
        if (index == -1) return false

        users[index] = users[index].copy(preferredRegion = newRegion)
        saveUsers(context, users)
        return true
    }

    // 삭제 함수
    fun deleteUser(context: Context, userId: Int): Boolean {
        val users = loadUsers(context).toMutableList()
        val removed = users.removeIf { it.id == userId }
        if (removed) {
            saveUsers(context, users)
        }
        return removed
    }

    // ID, PW 검증
    fun validateUser(context: Context, name: String, password: String): User? {
        val users = loadUsers(context)
        return users.find { it.name == name && it.password == password }
    }
}

/*
// ViewModel에서 사용
fun createNewUser(name: String, region: PreferredRegion) {
    val newUser = UserStorage.addUser(
        context = getApplication(),
        name = name,
        rank = UserRank.BRONZE,  // 기본 랭크
        region = region
    )
}

fun getUsersInRegion(region: PreferredRegion): List<User> {
    return UserStorage.getUsersByRegion(getApplication(), region)
}
 */