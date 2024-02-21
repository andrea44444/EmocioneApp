package com.example.emociones.database.repository

import com.example.emociones.database.models.Emotion
import kotlinx.coroutines.flow.Flow

interface EmotionsRepository {
    /**
     * Extraer los registros de Emociones de un Usuario especifico de los ultimos 30 dias
     */
    fun getEmotionStreamFromUser(idUser : Int, thirtyDaysAgo : String): Flow<List<Emotion>>

    /**
     * Extraer los registros de Emociones de un Usuario especifico de un rango de fechas especifico
     */
    fun getRangedDateEmotionStream(idUser : Int, date1 : String, date2 : String): Flow<List<Emotion>>

    /**
     * Insertar una Emocion
     */
    suspend fun insertEmotion(emotion: Emotion)
}