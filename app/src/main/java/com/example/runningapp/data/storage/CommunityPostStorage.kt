package com.example.runningapp.data.storage

import android.content.Context
import com.example.runningapp.data.CommunityPost
import com.example.runningapp.data.CommunityTag

object CommunityPostStorage : BaseStorage() {
    private const val FILENAME = "community_posts.json"

    // 기본적인 CRUD 작업
    fun savePosts(context: Context, posts: List<CommunityPost>) {
        saveToFile(context, FILENAME, posts)
    }

    fun loadPosts(context: Context): List<CommunityPost> {
        return loadFromFile(context, FILENAME) ?: emptyList()
    }

    fun getPostById(context: Context, postId: Int): CommunityPost? {
        return loadPosts(context).find { it.id == postId }
    }

    fun addPost(
        context: Context,
        title: String,
        content: String,
        tag: CommunityTag,
        userId: Int,
        imagePath: String? = null   // 여기 까지 parameter
    ): CommunityPost {
        val posts = loadPosts(context).toMutableList()
        val newPost = CommunityPost(
            id = generateNewId(context),
            title = title,
            content = content,
            tag = tag,
            userId = userId,
            imagePath = imagePath ?: "file:///android_asset/defaultPost.png"
        )
        posts.add(newPost)
        savePosts(context, posts)
        return newPost
    }

    // 필터링 함수들
    fun getPostsByTag(context: Context, tag: CommunityTag): List<CommunityPost> {
        return if (tag == CommunityTag.ALL) {
            loadPosts(context)
        } else {
            loadPosts(context).filter { it.tag == tag }
        }
    }

    fun getPostsByUserId(context: Context, userId: Int): List<CommunityPost> {
        return loadPosts(context).filter { it.userId == userId }
    }

    // 정렬 함수들
    fun getRecentPosts(context: Context, limit: Int = 10): List<CommunityPost> {
        return loadPosts(context)
            .sortedByDescending { it.createdAt }
            .take(limit)
    }

    fun getPopularPosts(context: Context, limit: Int = 10): List<CommunityPost> {
        return loadPosts(context)
            .sortedByDescending { it.likeCount }
            .take(limit)
    }

    // 업데이트 함수들
    fun updatePost(context: Context, postId: Int, title: String? = null, content: String? = null): Boolean {
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

    fun incrementLikeCount(context: Context, postId: Int): Boolean {
        val posts = loadPosts(context).toMutableList()
        val index = posts.indexOfFirst { it.id == postId }
        if (index == -1) return false

        posts[index] = posts[index].copy(
            likeCount = posts[index].likeCount + 1
        )
        savePosts(context, posts)
        return true
    }

    fun incrementCommentCount(context: Context, postId: Int): Boolean {
        val posts = loadPosts(context).toMutableList()
        val index = posts.indexOfFirst { it.id == postId }
        if (index == -1) return false

        posts[index] = posts[index].copy(
            commentCount = posts[index].commentCount + 1
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

    // 유틸리티 함수
    private fun generateNewId(context: Context): Int {
        return loadPosts(context).maxOfOrNull { it.id }?.plus(1) ?: 1
    }
}

/*
// ViewModel에서 사용
fun addNewPost(title: String, content: String, tag: CommunityTag) {
    CommunityPostStorage.addPost(
        context = getApplication(),
        title = title,
        content = content,
        tag = tag,
        userId = currentUserId
    )
}

fun getPopularPosts() {
    val posts = CommunityPostStorage.getPopularPosts(getApplication())
    _posts.value = posts
}
 */