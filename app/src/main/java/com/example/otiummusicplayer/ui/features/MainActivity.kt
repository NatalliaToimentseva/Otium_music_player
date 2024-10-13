package com.example.otiummusicplayer.ui.features

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.otiummusicplayer.appComponents.services.musicService.PlayerService
import com.example.otiummusicplayer.ui.navigation.AppGraph
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var playerService: PlayerService? = null

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

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, PlayerService::class.java)
        startForegroundService(intent)
//        bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
//        unbindService(this)
    }

//    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//        val myBinder = (service as? PlayerService.PlayerBinder)
//        playerService = myBinder?.service
//        this.toast("Start service $playerService")
//    }
//
//    override fun onServiceDisconnected(name: ComponentName?) {
//        playerService = null
//    }
}