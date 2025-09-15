package com.gado.madarsofttask.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.gado.madarsofttask.data.local.dao.UserDao
import com.gado.madarsofttask.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        
        private const val DATABASE_NAME = "user_database"
        
        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
        
        // For testing purposes
        fun getInMemoryDatabase(context: Context): UserDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                UserDatabase::class.java
            ).allowMainThreadQueries().build()
        }
    }
}