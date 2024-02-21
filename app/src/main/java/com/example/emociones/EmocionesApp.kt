package com.example.emociones

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.emociones.navigation.EmocionesNavHost

/**
 * Llamada al NavHost
 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EmocionesApp(
    navController: NavHostController = rememberNavController(),
    findLocation : ()-> Unit ,
) {
    EmocionesNavHost(
        navController = navController,
        findLocation = findLocation,
    )
}

