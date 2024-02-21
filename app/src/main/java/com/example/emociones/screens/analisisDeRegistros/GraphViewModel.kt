package com.example.emociones.screens.analisisDeRegistros

import androidx.lifecycle.ViewModel
import com.example.emociones.database.models.Emotion
import com.example.emociones.database.repository.EmotionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class GraphViewModel(
    private val emotionsRepository: EmotionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GraphUiState())
    val uiState: StateFlow<GraphUiState> = _uiState


    fun updateIdUser (idUser : Int) {
        _uiState.update {
            it.copy(idUser = idUser)
        }
    }

    fun updateLista (nuevaLista : List<Emotion>) {
        _uiState.update {
            it.copy(lista = nuevaLista)
        }
    }

    fun updateSubLista (nuevaLista : List<Emotion>) {
        _uiState.update {
            it.copy(subLista = nuevaLista)
        }
    }

    fun updateSelectedDate1 (date : String) {
        _uiState.update {
            it.copy(selectedDate1 = date)
        }

    }

    fun updateSelectedDate2 (date : String) {
        _uiState.update {
            it.copy(selectedDate2 = date)
        }
    }

    fun updateAnalisisTrigger (list : Map<String,Int>) {
        _uiState.update {
            it.copy(analisisTriggersSubLista = list)
        }
    }

    fun updateAnalisisLocation (list : Map<String,Int>) {
        _uiState.update {
            it.copy(analisisLocationSubLista = list)
        }
    }

    fun getSubLista(emocion : String){
        val lista = _uiState.value.lista
        val subLista = lista.filter { it.emocion == emocion }
        updateSubLista(subLista)
        updateAnalisisTrigger(subLista.groupBy { it.trigger }
            .mapValues { (_, list) -> list.size })
        updateAnalisisLocation(subLista.groupBy { it.location }
            .mapValues { (_, list) -> list.size })
    }

    suspend fun getEmotions(idUser : Int, thirtyDaysAgo : String) {
        val hayEmociones = emotionsRepository.getEmotionStreamFromUser(idUser, thirtyDaysAgo).firstOrNull()
        if (hayEmociones != null){
            emotionsRepository.getEmotionStreamFromUser(idUser, thirtyDaysAgo).collect { registros ->
                updateLista(registros)
                updateSubLista(registros)
            }
        }  else{
            updateLista(emptyList())
        }
    }

    suspend fun getEmotionsRanged(idUser : Int) {
        val date1 = _uiState.value.selectedDate1
        val date2 = _uiState.value.selectedDate2
        val hayEmociones = emotionsRepository.getRangedDateEmotionStream(idUser, date1, date2).firstOrNull()
        if (hayEmociones != null){
            emotionsRepository.getRangedDateEmotionStream(idUser, date1, date2).collect { registros ->
                updateLista(registros)
                updateSubLista(registros)
                updateAnalisisLocation(emptyMap())
                updateAnalisisTrigger(emptyMap())
            }
        } else{
            updateLista(emptyList())
        }
    }

    fun countEmotions(emotions: List<Emotion>): Map<String, Int> {
        val emotionCount = mutableMapOf<String, Int>()
        emotions.forEach { emotion ->
            val count = emotionCount.getOrDefault(emotion.emocion, 0)
            emotionCount[emotion.emocion] = count + 1
        }
        return emotionCount
    }

    fun convertirFechaAMillis(fechaString: String): Long {
        val fecha = LocalDate.parse(fechaString)
        val instant = fecha.atStartOfDay(ZoneId.of("UTC")).toInstant()
        return instant.toEpochMilli()
    }
}

data class GraphUiState(
    val idUser: Int = 0,
    var lista: List<Emotion> = emptyList(),
    var subLista: List<Emotion> = lista,
    var analisisTriggersSubLista : Map<String,Int> = emptyMap(),
    var analisisLocationSubLista : Map<String,Int> = emptyMap(),

    //Pair(hace 30 dias , dia de hoy)
    val today30: Pair<String, String> = Pair(
        LocalDate.now().format(formatter),
        LocalDate.now().minusDays(30).format(formatter)
    ),
    var selectedDate1 : String = today30.second,
    var selectedDate2 : String = today30.first,
)

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")