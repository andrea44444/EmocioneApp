package com.example.emociones.screens.agregarRegistroEmocion

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.emociones.navigation.Screen
import java.util.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.emociones.AppViewModelProvider
import com.example.emociones.locationPair
import com.example.emociones.data.ColoresPorDefecto
import com.example.emociones.data.EmocionesPorDefecto
import com.example.emociones.data.TriggersPorDefecto
import com.example.emociones.navigation.tabs
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Pantalla para un nuevo registro de Emocion
 *
 * Fecha de hoy
 * Formulario de Emocion
 * Menu entre las tres pantallas principales
 */

object EmotionScreenInfo : Screen {
    override val route = "Emotion"
    override val icon: ImageVector = Icons.Default.Add
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun Emotion (
    navigateToGraph: (Int) -> Unit,
    navigateToUser: (Int) -> Unit,
    reLoad: (Int) -> Unit,
    navController: NavController,
    findLocation : ()-> Unit,
    viewModel: NewEmotionViewModel = viewModel(factory = AppViewModelProvider.Factory),
){

    val coroutineScope = rememberCoroutineScope()

    //ID
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments
    val userId = arguments?.getInt(EmotionScreenInfo.userIdArg)

    if (userId != null) {
        viewModel.uiState.value.idUsuario = userId
        Scaffold(
            bottomBar  = {
                EmotionMenu(
                    navigateToGraph = navigateToGraph,
                    navigateToUser = navigateToUser,
                    userId = userId
                )
            }
        ) { innerPadding ->
            EmotionBody(
                uiState = viewModel.uiState.collectAsState().value,
                modifier = Modifier.padding(innerPadding),
                onEmotionChange = { emotion -> viewModel.updateEmotion(emotion) },
                onTriggerChange = { trigger -> viewModel.updateTrigger(trigger) },
                onLocationChange = {
                    findLocation()
                    viewModel.updateLocation(locationPair.toString())
                    coroutineScope.launch {
                        if (locationPair != null) {
                            viewModel.updateLocation(locationPair.toString())
                        }
                    }
                },
                onSaveClick = {
                    coroutineScope.launch {
                        if(viewModel.insertEmotion()){
                            reLoad(userId)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun EmotionBody(
    uiState: NewEmotionUiState,
    modifier: Modifier,
    onEmotionChange: (String) -> Unit,
    onTriggerChange: (String) -> Unit,
    onLocationChange: () -> Unit,
    onSaveClick: () -> Unit,
){
    //Sacar Fecha dia de Hoy
    val dateFormat = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale("es"))
    val date = LocalDate.now().format(dateFormat)

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = date)

        Formulario(
            modifier = modifier,
            onEmotionChange = onEmotionChange,
            onTriggerChange = onTriggerChange,
            onLocationChange = onLocationChange,
        )

        Button(
            onClick = onSaveClick,
            enabled = !uiState.emotion.isNullOrEmpty() && !uiState.trigger.isNullOrEmpty() && !uiState.localizacion.isNullOrEmpty(),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar")
        }
    }
}

@Composable
fun Formulario(
    modifier: Modifier,
    onEmotionChange: (String) -> Unit,
    onTriggerChange: (String) -> Unit,
    onLocationChange: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CampoEmocion(onEmotionChange = onEmotionChange)
        CampoTrigger(onTriggerChange = onTriggerChange)
        CampoLocalizacion(onLocationChange = onLocationChange)
    }
}

@Composable
fun CampoLocalizacion(
    onLocationChange: () -> Unit,
) {
    var localizacion by rememberSaveable { mutableStateOf("Localizacion") }

    Text(
        text = localizacion.takeIf { it.isNotBlank() } ?: "Localizacion",
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onLocationChange()
                    localizacion = locationPair.toString()
                }
            )
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
            .padding(18.dp)
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoEmocion(
    onEmotionChange: (String) -> Unit,
){
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedEmotion by rememberSaveable { mutableStateOf("") }
    var outlineBorderColor by remember { mutableStateOf(Color.Gray) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {isExpanded = it},
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedEmotion.takeIf { it.isNotBlank() } ?: "Emocion",
            onValueChange = {selectedEmotion = it },
            readOnly = true,
            modifier = Modifier
                .clickable(onClick = { isExpanded = true })
                .menuAnchor() // Modificador para anclar el menú desplegable al componente OutlinedTextField
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                //Bordes
                unfocusedIndicatorColor = outlineBorderColor,
                focusedIndicatorColor = outlineBorderColor,
                //Fondo
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
            )
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            EmocionesPorDefecto.getEmocionesPorDefecto().forEach {
                DropdownMenuItem(
                    text = { Text(text = it)},
                    onClick = {
                        onEmotionChange(it)
                        selectedEmotion = it
                        isExpanded = false
                        outlineBorderColor = ColoresPorDefecto.getColoresPorDefecto()[it]!!
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTrigger(
    onTriggerChange: (String) -> Unit,
){
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedTrigger by rememberSaveable { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {isExpanded = it},
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = selectedTrigger.takeIf { it.isNotBlank() } ?: "Trigger",
            onValueChange = { selectedTrigger = it },
            readOnly = true,
            modifier = Modifier
                .clickable(onClick = { isExpanded = true })
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            TriggersPorDefecto.getTriggersPorDefecto().forEach {
                DropdownMenuItem(
                    text = { Text(text = it)},
                    onClick = {
                        onTriggerChange(it)
                        selectedTrigger = it
                        isExpanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun EmotionMenu(
    navigateToGraph: (Int) -> Unit,
    navigateToUser:(Int) -> Unit,
    userId: Int
){
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        tabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = index == 1,
                onClick = {
                    when (index) {
                        0 -> navigateToGraph(userId)
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
fun PreviewHoyScreen(){
    EmotionBody(
        uiState = NewEmotionUiState(
            emotion = null,
            trigger = null,
            localizacion = null,
            idUsuario = 3
        ),
        modifier = Modifier,
        onEmotionChange = {},
        onTriggerChange = {},
        onLocationChange = {},
        onSaveClick = {},
    )
}