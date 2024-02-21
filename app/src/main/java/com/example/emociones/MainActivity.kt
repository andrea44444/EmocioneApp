package com.example.emociones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.emociones.localizacion.LocationManager
import com.example.emociones.ui.theme.EmocionesTheme
import android.Manifest
import android.util.Log

var locationPair: Pair<Double, Double>? = Pair(0.0, 0.0)

class MainActivity : ComponentActivity(){

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        ){
            requestLocationUpdates()
            locationPair?.let { (latitude, longitude) ->
                Log.d("Location", "b "+ latitude+ longitude)
            }
        } else {
            LocationManager.goSettingScreen(this)
        }
    }

    private fun requestLocationUpdates() {
        LocationManager.Builder
            .create(this@MainActivity)
            .request(onUpdateLocation = { latitude, longitude ->
                LocationManager.removeCallback(this@MainActivity)
                locationPair = Pair(latitude, longitude)
                locationPair?.let { (latitude, longitude) ->
                    Log.d("Location", "a "+ latitude+ longitude)
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EmocionesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmocionesApp(
                        findLocation = {
                            locationPermissionRequest.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        },
                    )
                }
            }
        }
    }
}




