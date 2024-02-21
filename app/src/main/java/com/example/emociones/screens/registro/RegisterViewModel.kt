package com.example.emociones.screens.registro

import androidx.lifecycle.ViewModel
import com.example.emociones.database.repository.UsersRepository
import com.example.emociones.database.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

data class RegisterUiState(
    val error: String? = null,
    val email: String? = null,
    val password: String? = null,
    val password2: String? = null,
    val userId: Int = 0,
)

class RegisterViewModel (private val usersRepository: UsersRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private fun updateUiState(
        error: String? = null,
        email: String? = null,
        password: String? = null,
        password2: String? = null,
        userId: Int = 0

    ) {
        _uiState.value = RegisterUiState(
            error = error,
            email = email,
            password = password,
            password2 = password2,
            userId = userId
        )
    }

    fun updateEmail(email: String?) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String?) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun updatePassword2(password2: String?) {
        _uiState.value = _uiState.value.copy(password2 = password2)
    }

    suspend fun insertUser() : Int{
        if(!_uiState.value.email.isNullOrEmpty() && !_uiState.value.password.isNullOrEmpty()){
            val email = _uiState.value.email ?: "error"
            val password = _uiState.value.password ?: "error"

            val user = User(0, email  , password)
            val existingUser = usersRepository.getUserByEmail(email).firstOrNull()

            if (existingUser == null) {
                val idI = usersRepository.insertUser(user)
                updateUiState(userId = idI.toInt())
                return _uiState.value.userId

            } else {
                updateUiState(error = "Ya hay una cuenta con ese correo " + existingUser.email)
            }
        }
        return _uiState.value.userId
    }
}