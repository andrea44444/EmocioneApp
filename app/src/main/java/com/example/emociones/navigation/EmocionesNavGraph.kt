package com.example.emociones.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.emociones.screens.agregarRegistroEmocion.Emotion
import com.example.emociones.screens.inicioSesion.Login
import com.example.emociones.screens.inicioSesion.LoginScreenInfo
import com.example.emociones.screens.registro.Register
import com.example.emociones.screens.registro.RegisterScreenInfo
import com.example.emociones.screens.agregarRegistroEmocion.EmotionScreenInfo
import com.example.emociones.screens.analisisDeRegistros.Graph
import com.example.emociones.screens.analisisDeRegistros.GraphScreenInfo
import com.example.emociones.screens.usuario.User
import com.example.emociones.screens.usuario.UserScreenInfo

/**
 * Maneja toda la navegacion entre las 5 pantallas que tiene la aplicacion
 */

@Composable
fun EmocionesNavHost (
    navController: NavHostController,
    findLocation :()-> Unit ,
){
    NavHost(
        navController = navController,
        startDestination = LoginScreenInfo.route,
    ) {
        composable(route = LoginScreenInfo.route) {
            Login(
                navigateToRegister = { navController.navigate(RegisterScreenInfo.route) },
                navigateToEmotion = {
                    navController.navigate("${EmotionScreenInfo.route}/${it}")
                }
            )
        }
        composable(route = RegisterScreenInfo.route) {
            Register(
                navigateToLogin = { navController.navigate(LoginScreenInfo.route) },
                navigateToEmotion = {
                    navController.navigate("${EmotionScreenInfo.route}/${it}")
                }
            )
        }
        composable(
            route = EmotionScreenInfo.routeWithArgs,
            arguments = listOf(navArgument(EmotionScreenInfo.userIdArg) {
                type = NavType.IntType
            })
        ) {
            Emotion(
                navigateToGraph = {
                    navController.navigate("${GraphScreenInfo.route}/${it}")
                },
                navigateToUser = {
                    navController.navigate("${UserScreenInfo.route}/${it}")
                },
                navController = navController,
                reLoad = {
                    navController.navigate("${EmotionScreenInfo.route}/${it}")
                },
                findLocation = findLocation,
                //location = location
            )
        }
        composable(
            route = GraphScreenInfo.routeWithArgs,
            arguments = listOf(navArgument(GraphScreenInfo.userIdArg) {
                type = NavType.IntType
            })
        ) {
            Graph(
                navigateToEmotion = {
                    navController.navigate("${EmotionScreenInfo.route}/${it}")
                },
                navigateToUser = {
                    navController.navigate("${UserScreenInfo.route}/${it}")
                },
                navController = navController
            )
        }
        composable(
            route = UserScreenInfo.routeWithArgs,
            arguments = listOf(navArgument(UserScreenInfo.userIdArg) {
                type = NavType.IntType
            })
        ) {
            User(
                navigateToLogin = { navController.navigate(LoginScreenInfo.route) },
                navigateToGraph = {
                    navController.navigate("${GraphScreenInfo.route}/${it}")
                },
                navigateToEmotion = {
                    navController.navigate("${EmotionScreenInfo.route}/${it}")
                },
                navController = navController
            )
        }
    }
}


