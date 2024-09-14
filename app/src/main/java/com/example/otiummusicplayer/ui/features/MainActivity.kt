package com.example.otiummusicplayer.ui.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.otiummusicplayer.ui.navigation.AppGraph
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OtiumMusicPlayerTheme {
                AppGraph(navHostController = rememberNavController())
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    OtiumMusicPlayerTheme {
//        Greeting("Android")
//    }
//}