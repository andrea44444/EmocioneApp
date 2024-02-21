package com.example.emociones

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.emociones.screens.inicioSesion.LoginViewModel
import com.example.emociones.screens.registro.RegisterViewModel
import com.example.emociones.screens.agregarRegistroEmocion.NewEmotionViewModel
import com.example.emociones.screens.analisisDeRegistros.GraphViewModel
import com.example.emociones.screens.usuario.UserViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire Emociones app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for LoginViewModel
        initializer {
            LoginViewModel(emocionesApplication().container.usersRepository)
        }

        // Initializer for RegisterViewModel
        initializer {
            RegisterViewModel(emocionesApplication().container.usersRepository)
        }

        // Initializer for EmotionViewModel
        initializer {
            NewEmotionViewModel(emocionesApplication().container.emotionsRepository)
        }

        // Initializer for GraphViewModel
        initializer {
            GraphViewModel(emocionesApplication().container.emotionsRepository)
        }

        // Initializer for UserViewModel
        initializer {
            UserViewModel(emocionesApplication().container.usersRepository)
        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [EmocionesApplication].
 */
fun CreationExtras.emocionesApplication(): EmocionesApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as EmocionesApplication)
