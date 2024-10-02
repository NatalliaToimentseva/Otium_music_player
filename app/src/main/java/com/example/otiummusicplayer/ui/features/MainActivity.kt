package com.example.otiummusicplayer.ui.features

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.example.otiummusicplayer.ui.navigation.AppGraph
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

private const val REQUEST_CODE = 0

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //permission
//        ActivityCompat.requestPermissions(this,
//            arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
//            REQUEST_CODE
//        )

        setContent {
            OtiumMusicPlayerTheme {
                AppGraph(navHostController = rememberNavController())
            }
        }
    }
}