package kz.tutorial.jsonplaceholdertypicode.domain.entity

data class Comment(
    val body: String,
    val email: String,
    val userId: Int,
    val postId: Int,
    val createdAt: String,
)