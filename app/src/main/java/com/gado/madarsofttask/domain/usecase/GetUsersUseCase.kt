package com.gado.madarsofttask.domain.usecase

import com.gado.madarsofttask.data.model.User
import com.gado.madarsofttask.domain.repository.UserRepository

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getAllUsers()
    }
}