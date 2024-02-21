package com.example.emociones.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem(val title: String, val icon: ImageVector)

// Guardo datos necesarios para la barra del menu de las tres pantallas principales
val tabs = listOf(
    TabItem("Graph", Icons.Default.Assessment),
    TabItem("Emotion", Icons.Default.Add),
    TabItem("User", Icons.Default.AccountCircle)
)

// Cada pantalla tiene su ruta, las tres pantallas principales necesitan el id del Usuario
interface Screen {
    val route: String
    val icon: ImageVector?
}