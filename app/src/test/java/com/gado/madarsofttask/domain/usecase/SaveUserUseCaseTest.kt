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

class SaveUserUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var saveUserUseCase: SaveUserUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        saveUserUseCase = SaveUserUseCase(userRepository)
    }

    @Test
    fun `should save valid user successfully`() = runTest {
        // Given
        val user = User(
            name = "John Doe",
            age = 30,
            job = "Developer",
            title = "Senior",
            gender = "Male"
        )
        whenever(userRepository.saveUser(user)).thenReturn(Result.success(Unit))

        // When
        val result = saveUserUseCase(user)

        // Then
        assertTrue(result.isSuccess)
        verify(userRepository).saveUser(user)
    }

    @Test
    fun `should fail when user name is empty`() = runTest {
        // Given
        val user = User(
            name = "",
            age = 30,
            job = "Developer",
            title = "Senior",
            gender = "Male"
        )

        // When
        val result = saveUserUseCase(user)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Invalid user data", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should fail when user age is zero or negative`() = runTest {
        // Given
        val user = User(
            name = "John Doe",
            age = 0,
            job = "Developer",
            title = "Senior",
            gender = "Male"
        )

        // When
        val result = saveUserUseCase(user)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Invalid user data", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should fail when user job is empty`() = runTest {
        // Given
        val user = User(
            name = "John Doe",
            age = 30,
            job = "",
            title = "Senior",
            gender = "Male"
        )

        // When
        val result = saveUserUseCase(user)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Invalid user data", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should fail when user title is empty`() = runTest {
        // Given
        val user = User(
            name = "John Doe",
            age = 30,
            job = "Developer",
            title = "",
            gender = "Male"
        )

        // When
        val result = saveUserUseCase(user)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Invalid user data", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should fail when user gender is empty`() = runTest {
        // Given
        val user = User(
            name = "John Doe",
            age = 30,
            job = "Developer",
            title = "Senior",
            gender = ""
        )

        // When
        val result = saveUserUseCase(user)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Invalid user data", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should propagate repository error`() = runTest {
        // Given
        val user = User(
            name = "John Doe",
            age = 30,
            job = "Developer",
            title = "Senior",
            gender = "Male"
        )
        val expectedError = Exception("Database error")
        whenever(userRepository.saveUser(user)).thenReturn(Result.failure(expectedError))

        // When
        val result = saveUserUseCase(user)

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}