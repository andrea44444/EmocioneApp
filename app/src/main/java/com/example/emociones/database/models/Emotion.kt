package com.example.emociones.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "registroEmociones",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Emotion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fecha: String,
    val emocion: String,
    val trigger : String,
    val location : String,
    val idUsuario : Int
)
