package com.gado.madarsofttask.data.repository

import com.gado.madarsofttask.data.datasource.UserDataSource
import com.gado.madarsofttask.data.model.User
import com.gado.madarsofttask.domain.repository.UserRepository
import java.util.UUID

class UserRepositoryImpl(private val dataSource: UserDataSource) : UserRepository {
    
    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            // Generate ID if not present (business logic)
            val userWithId = user.copy(id = if (user.id.isEmpty()) UUID.randomUUID().toString() else user.id)
            
            // Add user directly to database (more efficient than reading all + saving all)
            dataSource.addUser(userWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllUsers(): Result<List<User>> {
        return dataSource.getAllUsers()
    }
}