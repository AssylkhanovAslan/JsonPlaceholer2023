package kz.tutorial.jsonplaceholdertypicode.domain.request

data class AddCommentRequest(
    val Content: String,
    val PostID: Int?
)
