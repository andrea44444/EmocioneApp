package com.example.emociones.screens.agregarRegistroEmocion

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.emociones.database.models.Emotion
import com.example.emociones.database.repository.EmotionsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class NewEmotionUiState(
    val emotion: String? = null,
    val trigger: String? = null,
    val localizacion: String? = null,
    var idUsuario: Int = 0,
)

class NewEmotionViewModel (
    private val emotionsRepository: EmotionsRepository,
) : ViewModel(){

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = LocalDate.now().format(formatter)//"2024-01-14"

    private val _uiState = MutableStateFlow(NewEmotionUiState())
    val uiState: StateFlow<NewEmotionUiState> = _uiState

    fun updateEmotion(emotion: String?) {
        _uiState.value = _uiState.value.copy(emotion = emotion)
    }

    fun updateTrigger(trigger: String?) {
        _uiState.value = _uiState.value.copy(trigger = trigger)
    }

    fun updateLocation(localizacion: String?) {
        _uiState.value = _uiState.value.copy(localizacion = localizacion)
    }


    suspend fun insertEmotion() : Boolean{
        if(!_uiState.value.emotion.isNullOrEmpty() && !_uiState.value.trigger.isNullOrEmpty() && !_uiState.value.localizacion.isNullOrEmpty()){
            val emotion = _uiState.value.emotion ?: "error"
            val trigger = _uiState.value.trigger ?: "error"
            val location = _uiState.value.localizacion ?: "error"
            val userId = _uiState.value.idUsuario

            val registroEmotion = Emotion(0, fecha = today ,emocion = emotion, idUsuario = userId, location = location, trigger = trigger)
            emotionsRepository.insertEmotion(registroEmotion)

            return true
        }
        return false
    }
}