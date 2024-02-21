package com.example.emociones.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.emociones.database.models.Emotion
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionDao {

    @Query("SELECT * FROM registroEmociones WHERE idUsuario = :idUsuario AND fecha >= :thirtyDaysAgo ORDER BY fecha ASC")
    fun getEmotionsFrom30daysAgo(
        idUsuario: Int,
        thirtyDaysAgo: String
    ): Flow<List<Emotion>>

    @Query("SELECT * FROM registroEmociones WHERE idUsuario = :idUser AND fecha >= :date1 AND fecha <= :date2 ORDER BY fecha ASC")
    fun getEmotionsFromSpecificDates(
        idUser: Int,
        date1: String,
        date2: String
    ): Flow<List<Emotion>>

    // se ignora esa inserción y no se guarda la fila duplicada.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(emotion: Emotion)


}