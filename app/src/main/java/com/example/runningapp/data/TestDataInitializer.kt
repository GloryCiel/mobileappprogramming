package com.example.runningapp.data

import android.content.Context
import com.example.runningapp.data.storage.*
import java.util.Calendar

object TestDataInitializer {
    fun initializeIfNeeded(context: Context) {
        if (!checkIfJsonFilesExist(context)) {
            createTestData(context)
        }
    }

    private fun checkIfJsonFilesExist(context: Context): Boolean {
        val files = context.filesDir.list() ?: return false
        return files.contains("users.json") &&
                files.contains("courses.json") &&
                files.contains("community_posts.json") &&
                files.contains("crew_posts.json")
    }

    // 이 아래가 테스트 데이터
    private fun createTestData(context: Context) {
        // 날짜 설정 (2024-12-01 12:00:00)
        val testDate = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 1, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        /*
        context 이거 상세 내용 이런거 아님
        android.content 받는 거임

        추가로 post 에서 createdAt, updatedAt 등 시간은 알아서 들어가는 듯 함
        Models.kt에 System.currentTimeMillis()로 들어가 있음
         */

        val dummy_count = 4

        // 1. 테스트 유저 생성
        val users = mutableListOf<User>()
        for (index in 1..4) {
            val user = UserStorage.addUser(
                password = "test",
                context = context,
                name = "test_user$index",
                region = if (index % 2 == 0) PreferredRegion.SUSEONG_GU else PreferredRegion.JUNG_GU
            )
            if (user != null) {  // null 체크 추가
                users.add(user)
            }
        }

        // 2. 테스트 코스 생성
        val courses = mutableListOf<Course>()
        for (index in 1..4) {
            val course = CourseStorage.addCourse(
                context = context,
                title = "test_course$index",
                distance = (index * 10).toFloat(),
                description = "test_description$index",
                gpxFilePath = "test_gpx$index.gpx",
                userId = users[index % dummy_count].id,  // 1..4
                isPublic = true
            )
            courses.add(course)
        }

        // 3. 테스트 커뮤니티 게시글 생성
        val communitys = mutableListOf<CommunityPost>()
        for (index in 1..8) {
            val community = CommunityPostStorage.addPost(
                context = context,
                title = "test_community$index",
                content = "test_content$index",
                tag = if (index % 2 == 0) CommunityTag.TAG1 else CommunityTag.TAG2,
                userId = users[index % dummy_count].id,  // 1..4
            )
            communitys.add(community)
        }

        // 4. 테스트 크루 모집글 생성
        val crews = mutableListOf<CrewPost>()
        for (index in 1..8) {
            val crew = CrewPostStorage.addPost(
                context = context,
                title = "test_crew$index",
                content = "test_content$index",
                userId = users[index % dummy_count].id,  // 1..4
                courseId = courses[index % dummy_count].id,
                maxMembers = (index % 4) + 2  // 2..5
            )
            crews.add(crew)
        }
    }
}