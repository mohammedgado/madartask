package com.gado.madarsofttask.domain.repository

import com.gado.madarsofttask.data.model.User

interface UserRepository {
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun getAllUsers(): Result<List<User>>
}