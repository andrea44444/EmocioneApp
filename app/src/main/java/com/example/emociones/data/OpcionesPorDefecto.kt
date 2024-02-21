package com.example.emociones.data

import androidx.compose.ui.graphics.Color

object EmocionesPorDefecto {
    fun getEmocionesPorDefecto(): List<String> {
        return listOf("Felicidad", "Enfado", "Tristeza", "Ansiedad", "Aburrimiento", "Otro")
    }
}

object TriggersPorDefecto {
    fun getTriggersPorDefecto(): List<String> {
        return listOf("Familiares", "Pareja", "Amigos", "Trabajo", "Aspecto Físico", "Escuela", "Comida", "Dinero", "Otro")
    }
}

object ColoresPorDefecto {
    fun getColoresPorDefecto(): Map<String, Color> {
        return mapOf(
            "Felicidad" to Color.Cyan,
            "Tristeza" to Color.Blue,
            "Enfado" to Color.Red,
            "Ansiedad" to Color.Yellow,
            "Aburrimiento" to Color.Green,
            "Otro" to Color.LightGray,
        )
    }
}
