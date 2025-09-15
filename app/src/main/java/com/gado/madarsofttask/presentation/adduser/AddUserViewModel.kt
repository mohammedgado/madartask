package com.gado.madarsofttask.presentation.adduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gado.madarsofttask.data.model.User
import com.gado.madarsofttask.domain.usecase.SaveUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddUserViewModel(
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddUserUiState())
    val uiState: StateFlow<AddUserUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateAge(age: String) {
        val ageInt = age.toIntOrNull() ?: 0
        _uiState.value = _uiState.value.copy(age = age, ageValue = ageInt)
    }

    fun updateJob(job: String) {
        _uiState.value = _uiState.value.copy(job = job)
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun saveUser() {
        val currentState = _uiState.value
        
        if (!isValidInput(currentState)) {
            _uiState.value = currentState.copy(
                isLoading = false,
                errorMessage = "Please fill all fields correctly"
            )
            return
        }

        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val user = User(
                name = currentState.name,
                age = currentState.ageValue,
                job = currentState.job,
                title = currentState.title,
                gender = currentState.gender
            )

            saveUserUseCase(user)
                .onSuccess {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to save user"
                    )
                }
        }
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun isValidInput(state: AddUserUiState): Boolean {
        return state.name.isNotBlank() &&
                state.ageValue > 0 &&
                state.job.isNotBlank() &&
                state.title.isNotBlank() &&
                state.gender.isNotBlank()
    }
}

data class AddUserUiState(
    val name: String = "",
    val age: String = "",
    val ageValue: Int = 0,
    val job: String = "",
    val title: String = "",
    val gender: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)