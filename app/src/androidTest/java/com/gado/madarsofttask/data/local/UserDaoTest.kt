package com.gado.madarsofttask.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gado.madarsofttask.data.local.dao.UserDao
import com.gado.madarsofttask.data.local.database.UserDatabase
import com.gado.madarsofttask.data.local.entity.UserEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: UserDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            UserDatabase::class.java
        ).allowMainThreadQueries().build()
        
        userDao = database.userDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetUser() = runTest {
        // Given
        val user = UserEntity(
            id = "1",
            name = "John Doe",
            age = 30,
            job = "Developer",
            title = "Senior",
            gender = "Male"
        )

        // When
        userDao.insertUser(user)
        val retrievedUser = userDao.getUserById("1")

        // Then
        assertEquals(user, retrievedUser)
    }

    @Test
    fun getAllUsers() = runTest {
        // Given
        val users = listOf(
            UserEntity("1", "John Doe", 30, "Developer", "Senior", "Male"),
            UserEntity("2", "Jane Smith", 25, "Designer", "Junior", "Female")
        )

        // When
        userDao.insertUsers(users)
        val retrievedUsers = userDao.getAllUsers()

        // Then
        assertEquals(2, retrievedUsers.size)
        assertTrue(retrievedUsers.containsAll(users))
    }

    @Test
    fun deleteAllUsers() = runTest {
        // Given
        val users = listOf(
            UserEntity("1", "John Doe", 30, "Developer", "Senior", "Male"),
            UserEntity("2", "Jane Smith", 25, "Designer", "Junior", "Female")
        )
        userDao.insertUsers(users)

        // When
        userDao.deleteAllUsers()
        val retrievedUsers = userDao.getAllUsers()

        // Then
        assertTrue(retrievedUsers.isEmpty())
    }

    @Test
    fun replaceAllUsers() = runTest {
        // Given
        val initialUsers = listOf(
            UserEntity("1", "John Doe", 30, "Developer", "Senior", "Male")
        )
        val newUsers = listOf(
            UserEntity("2", "Jane Smith", 25, "Designer", "Junior", "Female"),
            UserEntity("3", "Bob Johnson", 35, "Manager", "Senior", "Male")
        )

        // When
        userDao.insertUsers(initialUsers)
        userDao.replaceAllUsers(newUsers)
        val retrievedUsers = userDao.getAllUsers()

        // Then
        assertEquals(2, retrievedUsers.size)
        assertTrue(retrievedUsers.containsAll(newUsers))
        assertNull(userDao.getUserById("1"))
    }

    @Test
    fun getUserCount() = runTest {
        // Given
        val users = listOf(
            UserEntity("1", "John Doe", 30, "Developer", "Senior", "Male"),
            UserEntity("2", "Jane Smith", 25, "Designer", "Junior", "Female")
        )

        // When
        userDao.insertUsers(users)
        val count = userDao.getUserCount()

        // Then
        assertEquals(2, count)
    }
}