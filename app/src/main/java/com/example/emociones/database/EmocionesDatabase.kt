package com.example.emociones.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.emociones.database.dao.EmotionDao
import com.example.emociones.database.dao.UserDao
import com.example.emociones.database.models.User
import com.example.emociones.database.models.Emotion

@Database(entities = [User::class, Emotion::class], version = 3, exportSchema = false)
abstract class EmocionesDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun emotionsDao(): EmotionDao

    companion object {
        @Volatile
        private var Instance: EmocionesDatabase? = null

        fun getDatabase(context: Context): EmocionesDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, EmocionesDatabase::class.java, "emociones_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}