package com.example.emociones.database.repository

import com.example.emociones.database.dao.EmotionDao
import com.example.emociones.database.models.Emotion
import kotlinx.coroutines.flow.Flow

class OfflineEmotionsRepository (private val emotionDao: EmotionDao) : EmotionsRepository {

    override fun getEmotionStreamFromUser(
        idUser : Int,
        thirtyDaysAgo : String
    ): Flow<List<Emotion>> = emotionDao.getEmotionsFrom30daysAgo(idUser, thirtyDaysAgo)

    override fun getRangedDateEmotionStream(
        idUser: Int,
        date1: String,
        date2: String
    ): Flow<List<Emotion>> = emotionDao.getEmotionsFromSpecificDates(idUser,date1,date2)


    override suspend fun insertEmotion(
        emotion: Emotion
    ) = emotionDao.insert(emotion)

}