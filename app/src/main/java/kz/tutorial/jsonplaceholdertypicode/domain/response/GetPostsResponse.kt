package kz.tutorial.jsonplaceholdertypicode.domain.response

import kz.tutorial.jsonplaceholdertypicode.domain.entity.Post

data class GetPostsResponse(
    val status: String,
    val data: List<Post>
)
