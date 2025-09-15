package com.gado.madarsofttask.data.repository

import com.gado.madarsofttask.data.datasource.UserDataSource
import com.gado.madarsofttask.data.model.User
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRepositoryImplTest {

    @Mock
    private lateinit var dataSource: UserDataSource

    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = UserRepositoryImpl(dataSource)
    }

    @Test
    fun `should save user with generated ID using addUser method`() = runTest {
        // Given
        val user = User(
            name = "John Doe",
            age = 30,
            job = "Developer",
            title = "Senior",
            gender = "Male"
        )
        whenever(dataSource.addUser(any())).thenReturn(Result.success(Unit))

        // When
        val result = repository.saveUser(user)

        // Then
        assertTrue(result.isSuccess)
        verify(dataSource).addUser(argThat { addedUser ->
            addedUser.name == "John Doe" && 
            addedUser.id.isNotEmpty()
        })
    }

    @Test
    fun `should preserve existing ID when user already has one`() = runTest {
        // Given
        val user = User(
            id = "existing-id",
            name = "John Doe",
            age = 30,
            job = "Developer",
            title = "Senior",
            gender = "Male"
        )
        whenever(dataSource.addUser(any())).thenReturn(Result.success(Unit))

        // When
        val result = repository.saveUser(user)

        // Then
        assertTrue(result.isSuccess)
        verify(dataSource).addUser(argThat { addedUser ->
            addedUser.id == "existing-id"
        })
    }

    @Test
    fun `should get all users through data source`() = runTest {
        // Given
        val expectedUsers = listOf(
            User(
                id = "1",
                name = "John Doe",
                age = 30,
                job = "Developer",
                title = "Senior",
                gender = "Male"
            )
        )
        whenever(dataSource.getAllUsers()).thenReturn(Result.success(expectedUsers))

        // When
        val result = repository.getAllUsers()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedUsers, result.getOrNull())
        verify(dataSource).getAllUsers()
    }

    @Test
    fun `should propagate data source error on save when adding user fails`() = runTest {
        // Given
        val user = User(
            name = "John Doe",
            age = 30,
            job = "Developer",
            title = "Senior",
            gender = "Male"
        )
        val expectedError = Exception("Storage error")
        whenever(dataSource.addUser(any())).thenReturn(Result.failure(expectedError))

        // When
        val result = repository.saveUser(user)

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }

    @Test
    fun `should propagate data source error on get all`() = runTest {
        // Given
        val expectedError = Exception("Storage error")
        whenever(dataSource.getAllUsers()).thenReturn(Result.failure(expectedError))

        // When
        val result = repository.getAllUsers()

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}