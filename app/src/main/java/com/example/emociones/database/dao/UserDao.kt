package com.example.emociones.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.emociones.database.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * from usuarios WHERE id = :id")
    fun getUserById(id: Int): Flow<User>

    @Query("SELECT * from usuarios WHERE email = :email")
    fun getUserByEmail(email: String): Flow<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User) : Long

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}