package com.example.emociones

import android.app.Application

class EmocionesApplication : Application() {

    /**
     * Una instancia de AppContainer usada por el resto de las clases para optener dependencias
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}