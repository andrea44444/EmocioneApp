package com.example.emociones.database.repository

import com.example.emociones.database.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    /**
     * Recibe un User segun el [id].
     */
    fun getUserById(id: Int): Flow<User?>

    /**
     * Recibe un User segun el [email].
     */
    fun getUserByEmail(email: String): Flow<User?>

    /**
     * Inserta un User
     */
    suspend fun insertUser(user: User): Long

    /**
     * Elimina un User
     */
    suspend fun deleteUser(user: User)

    /**
     * Actualiza un User
     * suspend fun updateUser(user: User)
     */

}