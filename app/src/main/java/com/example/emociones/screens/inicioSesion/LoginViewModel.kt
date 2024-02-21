package com.example.emociones.screens.inicioSesion

import androidx.lifecycle.ViewModel
import com.example.emociones.database.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

data class UserUiState(
    val error: String? = null,
    val userId: Int = 0,
    val email: String? = null,
    val password: String? = null
)

class LoginViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    fun updateUiState(userId: Int = 0, email: String? = null, password: String? = null, error: String? = null) {
        _uiState.value = UserUiState(
            error = error,
            userId = userId,
            email = email,
            password = password
        )
    }

    fun updateEmail(email: String?) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String?) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    suspend fun searchUser() : Int {
        if (!_uiState.value.email.isNullOrEmpty() && !_uiState.value.password.isNullOrEmpty()){
            val email = _uiState.value.email ?: "error"
            val password = _uiState.value.password ?: "error"

            val existingUser = usersRepository.getUserByEmail(email).firstOrNull()

            if (existingUser != null ) {
                if(existingUser.password == password){
                    updateUiState(userId = existingUser.id)
                    return existingUser.id
                }
            } else {
                updateUiState(error = "User not found")
            }
        }
        return 0
    }
}



