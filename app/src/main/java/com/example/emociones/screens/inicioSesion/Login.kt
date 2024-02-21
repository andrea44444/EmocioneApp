package com.example.emociones.screens.inicioSesion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.emociones.AppViewModelProvider
import com.example.emociones.navigation.Screen
import kotlinx.coroutines.launch

/**
 * Pantalla de inicio de sesion
 *
 * Formulario inicio Sesion
 * Boton de envio de usuario que lleva a pantalla de agregarRegistroEmocion
 * Link navegacion a pantalla registro
 */

object LoginScreenInfo : Screen {
    override val route = "Login"
    override val icon: ImageVector? = null
}

@Composable
fun Login (
    navigateToRegister: () -> Unit,
    navigateToEmotion: (Int) -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    LoginBody(
        userUiState = viewModel.uiState.collectAsState().value,
        onEmailChange = { email -> viewModel.updateEmail(email) },
        onPasswordChange = { password -> viewModel.updatePassword(password) },
        onLogInClick = {
            coroutineScope.launch {
                val id = viewModel.searchUser()
                if(id != 0){
                    navigateToEmotion(id)
                }
            }
        },
        onSingInClick = { navigateToRegister() },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    )
}

@Composable
fun LoginBody (
    userUiState: UserUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogInClick: () -> Unit,
    onSingInClick: () ->Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = LoginScreenInfo.route)

        InputUsuario(userUiState = userUiState, onEmailChange = onEmailChange, onPasswordChange = onPasswordChange)

        Button(
            onClick = {
                onLogInClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            enabled = !userUiState.email.isNullOrEmpty() && !userUiState.password.isNullOrEmpty()
        ) {
            Text("Login")
        }

        Text(
            text = "No tengo cuenta. Registrarme.",
            modifier = Modifier.clickable {
                onSingInClick()
            }
        )
    }
}

@Composable
fun InputUsuario (
    userUiState: UserUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
){
    OutlinedTextField(
        value = userUiState.email ?: "",
        onValueChange = onEmailChange,
        label = { Text("Email") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    OutlinedTextField(
        value = userUiState.password?: "",
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
@Preview
fun PreviewInicioSesion(){
    LoginBody(
        userUiState = UserUiState(
            error = "null",
            userId = 2,
            email = "juna",
            password = "null"
        ),
        onEmailChange = {},
        onPasswordChange = {},
        onLogInClick = {},
        onSingInClick = {},
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .fillMaxWidth()
    )
}