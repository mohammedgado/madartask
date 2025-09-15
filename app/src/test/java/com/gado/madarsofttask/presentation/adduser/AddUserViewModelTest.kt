package com.gado.madarsofttask.presentation.adduser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gado.madarsofttask.data.model.User
import com.gado.madarsofttask.domain.usecase.SaveUserUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class AddUserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var saveUserUseCase: SaveUserUseCase

    private lateinit var viewModel: AddUserViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = AddUserViewModel(saveUserUseCase)
    }

    @Test
    fun `should update name correctly`() {
        // When
        viewModel.updateName("John Doe")

        // Then
        assertEquals("John Doe", viewModel.uiState.value.name)
    }

    @Test
    fun `should update age correctly`() {
        // When
        viewModel.updateAge("30")

        // Then
        assertEquals("30", viewModel.uiState.value.age)
        assertEquals(30, viewModel.uiState.value.ageValue)
    }

    @Test
    fun `should handle invalid age input`() {
        // When
        viewModel.updateAge("invalid")

        // Then
        assertEquals("invalid", viewModel.uiState.value.age)
        assertEquals(0, viewModel.uiState.value.ageValue)
    }

    @Test
    fun `should update job correctly`() {
        // When
        viewModel.updateJob("Developer")

        // Then
        assertEquals("Developer", viewModel.uiState.value.job)
    }

    @Test
    fun `should update title correctly`() {
        // When
        viewModel.updateTitle("Senior")

        // Then
        assertEquals("Senior", viewModel.uiState.value.title)
    }

    @Test
    fun `should update gender correctly`() {
        // When
        viewModel.updateGender("Male")

        // Then
        assertEquals("Male", viewModel.uiState.value.gender)
    }

    @Test
    fun `should save user successfully with valid data`() = runTest {
        // Given
        viewModel.updateName("John Doe")
        viewModel.updateAge("30")
        viewModel.updateJob("Developer")
        viewModel.updateTitle("Senior")
        viewModel.updateGender("Male")
        
        whenever(saveUserUseCase(any())).thenReturn(Result.success(Unit))

        // When
        viewModel.saveUser()

        // Then
        assertTrue(viewModel.uiState.value.isSuccess)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
        verify(saveUserUseCase).invoke(any())
    }

    @Test
    fun `should show error when saving user with invalid data`() {
        // Given (empty name)
        viewModel.updateAge("30")
        viewModel.updateJob("Developer")
        viewModel.updateTitle("Senior")
        viewModel.updateGender("Male")

        // When
        viewModel.saveUser()

        // Then
        assertFalse(viewModel.uiState.value.isSuccess)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Please fill all fields correctly", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `should handle save user failure`() = runTest {
        // Given
        viewModel.updateName("John Doe")
        viewModel.updateAge("30")
        viewModel.updateJob("Developer")
        viewModel.updateTitle("Senior")
        viewModel.updateGender("Male")
        
        val errorMessage = "Save failed"
        whenever(saveUserUseCase(any())).thenReturn(Result.failure(Exception(errorMessage)))

        // When
        viewModel.saveUser()

        // Then
        assertFalse(viewModel.uiState.value.isSuccess)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `should clear success state`() {
        // Given
        viewModel.updateName("John Doe")
        viewModel.updateAge("30")
        viewModel.updateJob("Developer")
        viewModel.updateTitle("Senior")
        viewModel.updateGender("Male")

        // When
        viewModel.clearSuccess()

        // Then
        assertFalse(viewModel.uiState.value.isSuccess)
    }

    @Test
    fun `should clear error state`() {
        // Given
        viewModel.saveUser() // This will trigger validation error

        // When
        viewModel.clearError()

        // Then
        assertNull(viewModel.uiState.value.errorMessage)
    }
}