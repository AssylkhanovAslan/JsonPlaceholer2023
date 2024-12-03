package kz.tutorial.jsonplaceholdertypicode.domain.use_case

import kz.tutorial.jsonplaceholdertypicode.domain.entity.User

interface GetUsersUseCase {
    suspend fun invoke(): List<User>
}