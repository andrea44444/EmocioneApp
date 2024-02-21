package com.example.emociones.database.repository

import com.example.emociones.database.dao.UserDao
import com.example.emociones.database.models.User
import kotlinx.coroutines.flow.Flow

class OfflineUsersRepository (private val userDao: UserDao) : UsersRepository {

    override fun getUserById(id: Int): Flow<User?> = userDao.getUserById(id)

    override fun getUserByEmail(email: String): Flow<User?> = userDao.getUserByEmail(email)

    override suspend fun insertUser(user: User): Long = userDao.insert(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

    //override suspend fun updateUser(user: User) = userDao.update(user)
}