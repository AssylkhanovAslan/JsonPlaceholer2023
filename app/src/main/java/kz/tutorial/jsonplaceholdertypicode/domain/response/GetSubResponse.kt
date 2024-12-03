data class GetSubResponse(
    val status: String,
    val data: List<FollowerData>,
    val metadata: Metadata
)

data class FollowerData(
    val ID: Int,
    val CreatedAt: String,
    val UpdatedAt: String,
    val DeletedAt: String?,
    val FollowerID: Int,
    val FollowingID: Int
)

data class Metadata(
    val count: Int
)
