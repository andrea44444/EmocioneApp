package com.example.emociones.screens.registro

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.emociones.AppViewModelProvider
import com.example.emociones.navigation.Screen
import kotlinx.coroutines.launch

/**
 * Pantalla de registro
 *
 * Formulario registro
 * Boton de envio de formulario que lleva a pantalla de agregarRegistroEmocion
 * Link navegacion a pantalla Login
 */

object RegisterScreenInfo : Screen {
    override val route = "Register"
    override val icon: ImageVector? = null
}

@Composable
fun Register (
    navigateToLogin: () -> Unit,
    navigateToEmotion: (Int) -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    RegisterBody(
        userUiState = viewModel.uiState.collectAsState().value,
        onEmailChange = { email -> viewModel.updateEmail(email) },
        onPasswordChange = { password -> viewModel.updatePassword(password) },
        onPassword2Change = { password2 -> viewModel.updatePassword2(password2) },
        onSingInClick = {
            coroutineScope.launch {
                val id = viewModel.insertUser()
                if(id != 0){
                    Log.d("bd", "el id que mando es "+ id)
                    navigateToEmotion(id)
                } else {
                    Log.d("bd", "hay errores no hemos reubicado")
                }
            }
        },
        onLogInClick = {navigateToLogin()},
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    )
}

@Composable
fun RegisterBody (
    userUiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPassword2Change: (String) -> Unit,
    onLogInClick: () -> Unit,
    onSingInClick: () ->Unit,
    modifier: Modifier = Modifier
){
    val emailRegex = Regex(pattern = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Text(text = RegisterScreenInfo.route)

        InputRUsuario(
            userUiState = userUiState,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onPassword2Change = onPassword2Change
        )

        Button(
            onClick = {
                onSingInClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            enabled = !userUiState.email.isNullOrEmpty() &&
                    !userUiState.password.isNullOrEmpty() &&
                    !userUiState.password2.isNullOrEmpty() &&
                    userUiState.password == userUiState.password2 &&
                    userUiState.email.matches(emailRegex)
        ) {
            Text("Register")
        }

        Text(userUiState.error?: "")

        Text(
            text = "Ya tengo cuenta. Iniciar Sesion.",
            modifier = Modifier.clickable {
                onLogInClick()
            }
        )
    }
}

@Composable
fun InputRUsuario (
    userUiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPassword2Change: (String) -> Unit,
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
        value = userUiState.password ?: "",
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    OutlinedTextField(
        value = userUiState.password2 ?: "",
        onValueChange = onPassword2Change,
        label = { Text("Repeat Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}


@Composable
@Preview
fun PreviewRegistro(){
    RegisterBody(
        userUiState = RegisterUiState(
            email = "juna",
            password = "null",
            password2 = "null"
        ),
        onEmailChange = {},
        onPasswordChange = {},
        onPassword2Change = {},
        onLogInClick = {},
        onSingInClick = {},
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .fillMaxWidth()
    )
}