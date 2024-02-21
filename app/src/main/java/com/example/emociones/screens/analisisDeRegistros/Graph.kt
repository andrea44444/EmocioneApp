package com.example.emociones.screens.analisisDeRegistros

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.emociones.AppViewModelProvider
import com.example.emociones.database.models.Emotion
import com.example.emociones.navigation.Screen
import com.example.emociones.navigation.tabs
import com.example.emociones.screens.agregarRegistroEmocion.EmotionScreenInfo
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.emociones.data.ColoresPorDefecto
import kotlinx.coroutines.launch
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import java.time.Instant
import java.time.ZoneId

/**
 * Pantalla que muestra el analisis de los registros
 *
 * Selector de fechas
 * Llamada a CircularGraph
 * Lista de los registros segun las fechas seleccionadas
 */

object GraphScreenInfo : Screen {
    override val route = "Graph"
    override val icon: ImageVector = Icons.Default.Assessment
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun Graph (
    navigateToEmotion: (Int) -> Unit,
    navigateToUser:(Int) -> Unit,
    navController: NavController,
    viewModel: GraphViewModel = viewModel(factory = AppViewModelProvider.Factory),
){
    //ID
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments
    val userId = arguments?.getInt(EmotionScreenInfo.userIdArg)
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    if (userId != null) {
        viewModel.updateIdUser(userId)
        coroutineScope.launch {
            viewModel.getEmotions(userId, uiState.today30.second)
        }
        Scaffold(
            bottomBar  = {
                GraphMenu(
                    navigateToUser = navigateToUser,
                    navigateToEmotion = navigateToEmotion,
                    userId = userId
                )
            }
        ) { innerPadding ->
            GraphBody(
                modifier = Modifier.padding(innerPadding),
                sublista = uiState.subLista,
                calcularDatos = viewModel.countEmotions(uiState.lista),
                days30 = uiState.today30,
                convertToMillis = { viewModel.convertirFechaAMillis(it) },
                updateDate1 ={ viewModel.updateSelectedDate1(it) },
                updateDate2 ={ viewModel.updateSelectedDate2(it) },
                updateList = {
                    coroutineScope.launch {
                        viewModel.getEmotionsRanged(userId)
                    }
                },
                updateSubLista = {viewModel.getSubLista(it)},
                resetSubLista = {
                    viewModel.updateSubLista(uiState.lista)
                    viewModel.updateAnalisisLocation(emptyMap())
                    viewModel.updateAnalisisTrigger(emptyMap())
                },
                mapaTriggers = uiState.analisisTriggersSubLista,
                mapaLocation = uiState.analisisLocationSubLista,
            )
        }
    }
}

@Composable
fun GraphBody(
    modifier : Modifier,
    sublista : List<Emotion>,
    calcularDatos : Map<String, Int>,
    days30 : Pair<String,String>,
    convertToMillis : (String) -> Long,
    updateDate1 : (String) -> Unit,
    updateDate2 : (String) -> Unit,
    updateList : () -> Unit,
    updateSubLista : (String) -> Unit,
    resetSubLista : () -> Unit,
    mapaTriggers: Map<String, Int>,
    mapaLocation: Map<String, Int>
){
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Fechas(
            days30 = days30,
            convertToMillis = convertToMillis,
            updateDate1 = updateDate1,
            updateDate2 = updateDate2,
            updateList = updateList
        )
        Spacer(modifier = Modifier.height(20.dp))

        PieChart(
            data = calcularDatos,
            colorsMap = ColoresPorDefecto.getColoresPorDefecto(),
            updateSubLista = updateSubLista,
            resetSubLista = resetSubLista
        )

        EmotionList(
            lista = sublista,
            mapaTriggers = mapaTriggers,
            mapaLocation = mapaLocation
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Fechas (
    days30 : Pair<String,String>,
    convertToMillis : (String) -> Long,
    updateDate1 : (String) -> Unit,
    updateDate2 : (String) -> Unit,
    updateList : () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(Pair(false,1)) }
    val state1 = rememberDatePickerState(
        initialSelectedDateMillis = convertToMillis(days30.second)
    )
    val state2 = rememberDatePickerState(
        initialSelectedDateMillis = convertToMillis(days30.first)
    )
    val selectedDate1 = state1.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate() }
    val selectedDate2 = state2.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate() }
    val confirmEnabled = selectedDate1 != null && selectedDate2 != null && selectedDate1 < selectedDate2
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Text(
            text = selectedDate1?.toString().takeIf { it?.isNotBlank() == true } ?: days30.second,
            fontSize = 18.sp,
            modifier = Modifier
                .clickable(
                    onClick = {
                        showDialog = Pair(true, 1)
                    }
                )
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                .padding(18.dp)
        )

        Text(
            text = selectedDate2?.toString().takeIf { it?.isNotBlank() == true } ?: days30.first,
            fontSize = 18.sp,
            modifier = Modifier
                .clickable(
                    onClick = {
                        showDialog = Pair(true, 2)
                    }
                )
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                .padding(18.dp)
        )
    }


    if(showDialog.first){
        DatePickerDialog(
            onDismissRequest = { showDialog = Pair(false,1) },
            confirmButton = {
                Button(
                    onClick = {
                        updateList()
                        if (showDialog.second == 1){
                            updateDate1(selectedDate1.toString())
                        } else{
                            updateDate2(selectedDate2.toString())
                        }
                        showDialog = Pair(false,1)
                    },
                    enabled = confirmEnabled,
                    modifier = Modifier
                ) {
                    Text(text = "Confirmar")
                }
            },
        ) {
            DatePicker(
                state = if (showDialog.second == 1) state1 else state2,
            )
        }
    }
}


@Composable
fun EmotionList (
    lista : List<Emotion>,
    mapaTriggers : Map<String,Int>,
    mapaLocation : Map<String,Int>
) {
    if (lista.isEmpty()) {
        Text(text = "No hay datos")
    } else {
        if(mapaTriggers.isNotEmpty()){
            Card (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    Text(
                        text = "Triggers:",
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    mapaTriggers.forEach { (trigger, count) ->
                        Text(
                            text = "$trigger: $count",
                            modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }

            Card (
                modifier = Modifier.fillMaxWidth()
            ){
                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    Text(
                        text = "Locations:",
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    mapaLocation.forEach { (location, count) ->
                        Text(
                            text = "$location: $count",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        lista.forEach { emocion ->
            EmotionCard(emocion = emocion)
        }
    }
}

@Composable
fun EmotionCard (emocion: Emotion) {
    val color = ColoresPorDefecto.getColoresPorDefecto()[emocion.emocion] ?: Color.Gray
    Card(
        border = BorderStroke(2.dp,color),
        colors = CardColors(containerColor = Color.Transparent, contentColor = Color.Black,
            disabledContentColor = Color.Transparent, disabledContainerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        )
        {
            Column (
                modifier = Modifier.padding(16.dp)
            ){
                Text(text = emocion.trigger)
                Text(text = emocion.location)

            }
            Box{
                Text(
                    text = emocion.fecha,
                    fontWeight = FontWeight.Medium
                )
            }
        }

    }
}

@Composable
fun GraphMenu(
    navigateToUser:(Int) -> Unit,
    navigateToEmotion:(Int) -> Unit,
    userId: Int
){
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        tabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = index == 0,
                onClick = {
                    when (index) {
                        1 -> navigateToEmotion(userId)
                        2 -> navigateToUser(userId)
                    }
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        modifier = Modifier.size(36.dp)
                    )
                },
            )
        }
    }
}

@Composable
@Preview
fun PreviewGraphScreen(){
    val emotions = listOf(
        Emotion(fecha = "2024-02-18", emocion = "Felicidad", trigger = "Evento feliz", location = "Ciudad A", idUsuario = 2),
        Emotion(fecha = "2024-02-17", emocion = "Tristeza", trigger = "Evento triste", location = "Ciudad B", idUsuario = 2),
        Emotion(fecha = "2024-02-16", emocion = "Ira", trigger = "Evento enojado", location = "Ciudad C", idUsuario = 2)
    )
    GraphBody(
        modifier = Modifier,
        sublista = emotions,
        calcularDatos = mapOf(
            "Felicidad" to 1,
            "Tristeza" to 1,
            "Enfado" to 2
        ),
        days30 = Pair("21/01/2024","20/02/2024"),
        convertToMillis = { 0L },
        updateDate1 = {},
        updateDate2 = {},
        updateList = {},
        updateSubLista = {},
        resetSubLista = {},
        mapaTriggers = emptyMap(),
        mapaLocation = emptyMap()
    )
}