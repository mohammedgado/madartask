package com.gado.madarsofttask.presentation.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gado.madarsofttask.data.model.User
import com.gado.madarsofttask.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class UserListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getUsersUseCase: GetUsersUseCase

    private lateinit var viewModel: UserListViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `should load users successfully on init`() = runTest {
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
        whenever(getUsersUseCase()).thenReturn(Result.success(expectedUsers))

        // When
        viewModel = UserListViewModel(getUsersUseCase)

        // Then
        assertEquals(expectedUsers, viewModel.uiState.value.users)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
        verify(getUsersUseCase).invoke()
    }

    @Test
    fun `should handle empty users list`() = runTest {
        // Given
        whenever(getUsersUseCase()).thenReturn(Result.success(emptyList()))

        // When
        viewModel = UserListViewModel(getUsersUseCase)

        // Then
        assertTrue(viewModel.uiState.value.users.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `should handle load users failure`() = runTest {
        // Given
        val errorMessage = "Load failed"
        whenever(getUsersUseCase()).thenReturn(Result.failure(Exception(errorMessage)))

        // When
        viewModel = UserListViewModel(getUsersUseCase)

        // Then
        assertTrue(viewModel.uiState.value.users.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `should reload users when loadUsers is called`() = runTest {
        // Given
        val initialUsers = listOf(
            User(
                id = "1",
                name = "John Doe",
                age = 30,
                job = "Developer",
                title = "Senior",
                gender = "Male"
            )
        )
        whenever(getUsersUseCase()).thenReturn(Result.success(initialUsers))
        viewModel = UserListViewModel(getUsersUseCase)

        val updatedUsers = listOf(
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
        whenever(getUsersUseCase()).thenReturn(Result.success(updatedUsers))

        // When
        viewModel.loadUsers()

        // Then
        assertEquals(updatedUsers, viewModel.uiState.value.users)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `should clear error message`() = runTest {
        // Given
        whenever(getUsersUseCase()).thenReturn(Result.failure(Exception("Error")))
        viewModel = UserListViewModel(getUsersUseCase)

        // When
        viewModel.clearError()

        // Then
        assertNull(viewModel.uiState.value.errorMessage)
    }
}