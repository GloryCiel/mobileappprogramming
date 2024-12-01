package com.example.runningapp.data.storage

import android.content.Context
import com.example.runningapp.data.Course

object CourseStorage : BaseStorage() {
    private const val FILENAME = "courses.json"

    // 기본적인 CRUD 작업
    fun saveCourses(context: Context, courses: List<Course>) {
        saveToFile(context, FILENAME, courses)
    }

    fun loadCourses(context: Context): List<Course> {
        return loadFromFile(context, FILENAME) ?: emptyList()
    }

    fun getCourseById(context: Context, courseId: Int): Course? {
        return loadCourses(context).find { it.id == courseId }
    }

    fun addCourse(
        context: Context,
        title: String,
        distance: Float,
        description: String,
        gpxFilePath: String,
        userId: Int,
        courseImagePath: String? = null,
        isPublic: Boolean = true
    ): Course {
        val courses = loadCourses(context).toMutableList()
        val newCourse = Course(
            id = getNextId(context, FILENAME),  // 자동으로 다음 ID 생성
            title = title,
            distance = distance,
            description = description,
            gpxFilePath = gpxFilePath,
            userId = userId,
            courseImagePath = courseImagePath ?: "file:///android_asset/defaultCourse.png",
            isPublic = isPublic
        )
        courses.add(newCourse)
        saveCourses(context, courses)
        return newCourse
    }

    // 필터링 함수들
    fun getCoursesByUserId(context: Context, userId: Int): List<Course> {
        return loadCourses(context).filter { it.userId == userId }
    }

    fun getPublicCourses(context: Context): List<Course> {
        return loadCourses(context).filter { it.isPublic }
    }

    // 거리 기반 필터링
    fun getCoursesByDistance(context: Context, minDistance: Float, maxDistance: Float): List<Course> {
        return loadCourses(context)
            .filter { it.distance in minDistance..maxDistance }
    }

    // 정렬 함수들
    fun getRecentCourses(context: Context, limit: Int = 10): List<Course> {
        return loadCourses(context)
            .sortedByDescending { it.date }
            .take(limit)
    }

    fun getCoursesByDistanceAsc(context: Context): List<Course> {
        return loadCourses(context).sortedBy { it.distance }
    }

    fun getCoursesByDistanceDesc(context: Context): List<Course> {
        return loadCourses(context).sortedByDescending { it.distance }
    }

    // 업데이트 함수들
    fun updateCourse(
        context: Context,
        courseId: Int,
        title: String? = null,
        description: String? = null,
        isPublic: Boolean? = null
    ): Boolean {
        val courses = loadCourses(context).toMutableList()
        val index = courses.indexOfFirst { it.id == courseId }
        if (index == -1) return false

        courses[index] = courses[index].copy(
            title = title ?: courses[index].title,
            description = description ?: courses[index].description,
            isPublic = isPublic ?: courses[index].isPublic
        )
        saveCourses(context, courses)
        return true
    }

    fun updateCourseImage(context: Context, courseId: Int, newImagePath: String): Boolean {
        val courses = loadCourses(context).toMutableList()
        val index = courses.indexOfFirst { it.id == courseId }
        if (index == -1) return false

        courses[index] = courses[index].copy(courseImagePath = newImagePath)
        saveCourses(context, courses)
        return true
    }

    // 삭제 함수
    fun deleteCourse(context: Context, courseId: Int): Boolean {
        val courses = loadCourses(context).toMutableList()
        val removed = courses.removeIf { it.id == courseId }
        if (removed) {
            saveCourses(context, courses)
        }
        return removed
    }

    // 검색 함수
    fun searchCourses(context: Context, query: String): List<Course> {
        return loadCourses(context).filter { course ->
            course.title.contains(query, ignoreCase = true) ||
                    course.description.contains(query, ignoreCase = true)
        }
    }
}

/*
// ViewModel에서 사용
fun addNewCourse(
    title: String,
    distance: Float,
    description: String,
    gpxFilePath: String
) {
    CourseStorage.addCourse(
        context = getApplication(),
        title = title,
        distance = distance,
        description = description,
        gpxFilePath = gpxFilePath,
        userId = currentUserId
    )
}

fun getShortCourses() {
    val courses = CourseStorage.getCoursesByDistance(
        context = getApplication(),
        minDistance = 0f,
        maxDistance = 5000f  // 5km 이하
    )
    _courses.value = courses
}

fun searchCourses(query: String) {
    val results = CourseStorage.searchCourses(getApplication(), query)
    _searchResults.value = results
}
 */