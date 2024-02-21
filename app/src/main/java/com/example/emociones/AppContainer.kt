package com.example.emociones

import android.content.Context
import com.example.emociones.database.EmocionesDatabase
import com.example.emociones.database.repository.EmotionsRepository
import com.example.emociones.database.repository.OfflineEmotionsRepository
import com.example.emociones.database.repository.OfflineUsersRepository
import com.example.emociones.database.repository.UsersRepository

interface AppContainer {
    val usersRepository: UsersRepository
    val emotionsRepository : EmotionsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineUsersRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    /**
     * Implementation for [UsersRepository]
     */
    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(EmocionesDatabase.getDatabase(context).userDao())
    }

    /**
     * Implementation for [EmotionsRepository]
     */
    override val emotionsRepository: EmotionsRepository by lazy {
        OfflineEmotionsRepository(EmocionesDatabase.getDatabase(context).emotionsDao())
    }
}