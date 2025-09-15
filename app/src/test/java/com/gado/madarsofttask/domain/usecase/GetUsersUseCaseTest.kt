package com.gado.madarsofttask.domain.usecase

import com.gado.madarsofttask.data.model.User
import com.gado.madarsofttask.domain.repository.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetUsersUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getUsersUseCase: GetUsersUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getUsersUseCase = GetUsersUseCase(userRepository)
    }

    @Test
    fun `should return users successfully`() = runTest {
        // Given
        val expectedUsers = listOf(
            User(
                id = "1",
                name = "John Doe",
                age = 30,
                job = "Developer",
                title = "Senior",
                gender = "Male"
            ),
            User(
                id = "2",
                name = "Jane Smith",
                age = 25,
                job = "Designer",
                title = "Junior",
                gender = "Female"
            )
        )
        whenever(userRepository.getAllUsers()).thenReturn(Result.success(expectedUsers))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedUsers, result.getOrNull())
        verify(userRepository).getAllUsers()
    }

    @Test
    fun `should return empty list when no users exist`() = runTest {
        // Given
        whenever(userRepository.getAllUsers()).thenReturn(Result.success(emptyList()))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrNull())
        verify(userRepository).getAllUsers()
    }

    @Test
    fun `should propagate repository error`() = runTest {
        // Given
        val expectedError = Exception("Database error")
        whenever(userRepository.getAllUsers()).thenReturn(Result.failure(expectedError))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
        verify(userRepository).getAllUsers()
    }
}