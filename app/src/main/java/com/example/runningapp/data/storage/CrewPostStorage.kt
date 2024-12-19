package com.example.runningapp.data.storage

import android.content.Context
import com.example.runningapp.data.CrewPost

object CrewPostStorage : BaseStorage() {
    private const val FILENAME = "crew_posts.json"

    // 기본적인 CRUD 작업
    fun savePosts(context: Context, posts: List<CrewPost>) {
        saveToFile(context, FILENAME, posts)
    }

    fun loadPosts(context: Context): List<CrewPost> {
        return loadFromFile(context, FILENAME) ?: emptyList()
    }

    fun getPostById(context: Context, postId: Int): CrewPost? {
        return loadPosts(context).find { it.id == postId }
    }

    fun addPost(
        context: Context,
        title: String,
        content: String,
        location: String,
        userId: Int,
        courseId: Int?,
        maxMembers: Int? = null,
        imagePath: String? = null
    ): CrewPost {
        val posts = loadPosts(context).toMutableList()
        val newPost = CrewPost(
            id = getNextId(context, FILENAME),  // 자동으로 다음 ID 생성
            title = title,
            content = content,
            location = location,
            userId = userId,
            courseId = courseId,
            maxMembers = maxMembers,
            imagePath = imagePath ?: "file:///android_asset/defaultPost.png"
        )
        posts.add(newPost)
        savePosts(context, posts)
        return newPost
    }

    // 필터링 함수들
    fun getPostsByUserId(context: Context, userId: Int): List<CrewPost> {
        return loadPosts(context).filter { it.userId == userId }
    }

    fun getPostsByCourseId(context: Context, courseId: Int): List<CrewPost> {
        return loadPosts(context).filter { it.courseId == courseId }
    }

    fun getAvailableCrewPosts(context: Context): List<CrewPost> {
        return loadPosts(context).filter { post ->
            post.maxMembers?.let { max ->
                post.currentMembers < max
            } ?: true
        }
    }

    // 정렬 함수들
    fun getRecentPosts(context: Context, limit: Int = 10): List<CrewPost> {
        return loadPosts(context)
            .sortedByDescending { it.createdAt }
            .take(limit)
    }

    // 업데이트 함수들
    fun updatePost(
        context: Context,
        postId: Int,
        title: String? = null,
        content: String? = null
    ): Boolean {
        val posts = loadPosts(context).toMutableList()
        val index = posts.indexOfFirst { it.id == postId }
        if (index == -1) return false

        posts[index] = posts[index].copy(
            title = title ?: posts[index].title,
            content = content ?: posts[index].content,
            updatedAt = System.currentTimeMillis()
        )
        savePosts(context, posts)
        return true
    }

    fun addCrewMember(context: Context, postId: Int): Boolean {
        val posts = loadPosts(context).toMutableList()
        val index = posts.indexOfFirst { it.id == postId }
        if (index == -1) return false

        val post = posts[index]
        if (post.maxMembers != null && post.currentMembers >= post.maxMembers) {
            return false
        }

        posts[index] = post.copy(
            currentMembers = post.currentMembers + 1
        )
        savePosts(context, posts)
        return true
    }

    fun removeCrewMember(context: Context, postId: Int): Boolean {
        val posts = loadPosts(context).toMutableList()
        val index = posts.indexOfFirst { it.id == postId }
        if (index == -1) return false

        val post = posts[index]
        if (post.currentMembers <= 1) {
            return false
        }

        posts[index] = post.copy(
            currentMembers = post.currentMembers - 1
        )
        savePosts(context, posts)
        return true
    }

    // 삭제 함수
    fun deletePost(context: Context, postId: Int): Boolean {
        val posts = loadPosts(context).toMutableList()
        val removed = posts.removeIf { it.id == postId }
        if (removed) {
            savePosts(context, posts)
        }
        return removed
    }
}

/*
// ViewModel에서 사용
fun createCrewPost(title: String, content: String, courseId: Int?, maxMembers: Int?) {
    CrewPostStorage.addPost(
        context = getApplication(),
        title = title,
        content = content,
        userId = currentUserId,
        courseId = courseId,
        maxMembers = maxMembers
    )
}

fun joinCrew(postId: Int): Boolean {
    return CrewPostStorage.addCrewMember(getApplication(), postId)
}

fun getAvailableCrews() {
    val posts = CrewPostStorage.getAvailableCrewPosts(getApplication())
    _crewPosts.value = posts
}
 */