package com.example.emociones.screens.usuario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.emociones.AppViewModelProvider
import com.example.emociones.navigation.Screen
import com.example.emociones.navigation.tabs
import com.example.emociones.screens.agregarRegistroEmocion.EmotionScreenInfo
import kotlinx.coroutines.launch

/**
 * Pantalla de Usuario
 *
 * Email de la cuenta
 * Boton de cerrar sesion que lleva a pantalla de Login
 * Boton de eliminar cuenta que lleva a pantalla Login ademas de eliminar
 */

object UserScreenInfo : Screen {
    override val route = "User"
    override val icon: ImageVector? = Icons.Default.AccountCircle
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun User (
    navigateToGraph: (Int) -> Unit,
    navigateToEmotion:(Int) -> Unit,
    navigateToLogin: () -> Unit,
    navController: NavController,
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
){
    //ID
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments
    val userId = arguments?.getInt(EmotionScreenInfo.userIdArg)
    val coroutineScope = rememberCoroutineScope()


    if (userId != null) {
        coroutineScope.launch {
            viewModel.getEmail(userId)
        }
        Scaffold(
            bottomBar  = {
                UserMenu(
                    navigateToGraph = navigateToGraph,
                    navigateToEmotion = navigateToEmotion,
                    userId = userId
                )
            }
        ) { innerPadding ->
            UserBody(
                userEmail = viewModel.userEmail,
                navigateToLogin = navigateToLogin,
                modifier = Modifier.padding(innerPadding),
                deleteUser = {
                    coroutineScope.launch {
                        viewModel.deleteUser(userId)
                    }
                }
            )
        }
    }
}

@Composable
fun UserBody(
    userEmail : String,
    navigateToLogin : () -> Unit,
    modifier : Modifier,
    deleteUser : () -> Unit
){
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UserCard(userEmail = userEmail)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = navigateToLogin,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cerrar Sesion")
        }

        Button(
            onClick = {
                deleteUser()
                navigateToLogin()
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Gray
            )
        ) {
            Text(
                text = "Eliminar Cuenta",

            )
        }
    }
}
@Composable
fun UserCard(
    userEmail : String,
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Foto usuario",
            modifier = Modifier.size(100.dp))
        Text(text = "Correo: " + userEmail)
    }
}

@Composable
fun UserMenu(
    navigateToGraph: (Int) -> Unit,
    navigateToEmotion:(Int) -> Unit,
    userId: Int
){
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        tabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = index == 2,
                onClick = {
                    when (index) {
                        0 -> navigateToGraph(userId)
                        1 -> navigateToEmotion(userId)
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
fun PreviewUserScreen(){
    UserBody(
        userEmail = "email@gmail.com",
        navigateToLogin = {},
        modifier = Modifier,
        deleteUser = {}
    )
}
