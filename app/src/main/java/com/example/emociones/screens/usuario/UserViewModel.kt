package com.example.emociones.screens.usuario

import androidx.lifecycle.ViewModel
import com.example.emociones.database.repository.UsersRepository
import kotlinx.coroutines.flow.firstOrNull

class UserViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    var userEmail: String = ""
        private set

    suspend fun deleteUser(userId : Int){
        val user = usersRepository.getUserById(userId).firstOrNull()
        if (user != null) {
            usersRepository.deleteUser(user)
        }
    }

    suspend fun getEmail(userId : Int){
        val user = usersRepository.getUserById(userId).firstOrNull()
        userEmail = user?.email ?: ""
    }

}