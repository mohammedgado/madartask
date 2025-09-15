package com.gado.madarsofttask.data.datasource

import com.gado.madarsofttask.data.model.User

interface UserDataSource {
    suspend fun saveUsers(users: List<User>): Result<Unit>
    suspend fun getAllUsers(): Result<List<User>>
    suspend fun addUser(user: User): Result<Unit>
}