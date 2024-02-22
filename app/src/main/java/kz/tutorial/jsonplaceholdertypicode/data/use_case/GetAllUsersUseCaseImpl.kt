package kz.tutorial.jsonplaceholdertypicode.data.use_case

import kz.tutorial.jsonplaceholdertypicode.domain.entity.User
import kz.tutorial.jsonplaceholdertypicode.domain.repository.UserRepository
import kz.tutorial.jsonplaceholdertypicode.domain.use_case.GetAllUsersUseCase

class GetAllUsersUseCaseImpl (private val usersRepository:UserRepository) : GetAllUsersUseCase {
    override suspend fun invoke(): List<User> {
        return usersRepository.getUsers()
    }
}