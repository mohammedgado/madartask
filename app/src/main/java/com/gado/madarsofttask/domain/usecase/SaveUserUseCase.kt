package com.gado.madarsofttask.domain.usecase

import com.gado.madarsofttask.data.model.User
import com.gado.madarsofttask.domain.repository.UserRepository

class SaveUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return if (isValidUser(user)) {
            userRepository.saveUser(user)
        } else {
            Result.failure(IllegalArgumentException("Invalid user data"))
        }
    }
    
    private fun isValidUser(user: User): Boolean {
        return user.name.isNotBlank() && 
               user.age > 0 && 
               user.job.isNotBlank() && 
               user.title.isNotBlank() && 
               user.gender.isNotBlank()
    }
}