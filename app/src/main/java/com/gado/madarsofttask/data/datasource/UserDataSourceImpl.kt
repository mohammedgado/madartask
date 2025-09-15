package com.gado.madarsofttask.data.datasource

import android.content.Context
import com.gado.madarsofttask.data.local.database.UserDatabase
import com.gado.madarsofttask.data.local.mapper.toEntity
import com.gado.madarsofttask.data.local.mapper.toEntityList
import com.gado.madarsofttask.data.local.mapper.toModelList
import com.gado.madarsofttask.data.model.User

class UserDataSourceImpl(context: Context) : UserDataSource {
    
    private val userDao = UserDatabase.getDatabase(context).userDao()
    
    override suspend fun saveUsers(users: List<User>): Result<Unit> {
        return try {
            val userEntities = users.toEntityList()
            userDao.replaceAllUsers(userEntities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val userEntities = userDao.getAllUsers()
            val users = userEntities.toModelList()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun addUser(user: User): Result<Unit> {
        return try {
            val userEntity = user.toEntity()
            userDao.insertUser(userEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}