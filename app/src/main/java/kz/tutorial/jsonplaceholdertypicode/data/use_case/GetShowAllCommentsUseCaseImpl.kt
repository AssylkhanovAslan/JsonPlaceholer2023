package kz.tutorial.jsonplaceholdertypicode.data.use_case

import kz.tutorial.jsonplaceholdertypicode.domain.entity.Comment
import kz.tutorial.jsonplaceholdertypicode.domain.repository.PostsRepository
import kz.tutorial.jsonplaceholdertypicode.domain.use_case.GetShowAllCommentsUseCase

class GetShowAllCommentsUseCaseImpl(private val repo: PostsRepository):GetShowAllCommentsUseCase {
    override suspend fun invoke(postId: Int): List<Comment> {
        return repo.getPostComments(postId)
    }
}