package com.example.otiummusicplayer.ui.features

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.otiummusicplayer.ui.navigation.AppGraph
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OtiumMusicPlayerTheme(dynamicColor = false) {
                AppGraph(navHostController = rememberNavController())
            }
        }
    }
}