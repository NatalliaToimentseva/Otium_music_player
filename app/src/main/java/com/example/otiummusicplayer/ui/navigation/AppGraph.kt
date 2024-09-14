package com.example.otiummusicplayer.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.otiummusicplayer.ui.features.playTrack.PlayTrackScreen
import com.example.otiummusicplayer.ui.features.search.screens.main.NetworkSearchScreen
import com.example.otiummusicplayer.ui.features.search.screens.tracks.ShowTrackList

private const val TRACK_ID = "id"
private const val TRACK_IMG_URL = "imgUrl"
private const val TRACK_TITLE = "title"
private const val TRACK_AUDIO_URL = "audioUrl"

@Composable
fun AppGraph(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = "NetworkSearchScreen") {
        composable("NetworkSearchScreen") {
            NetworkSearchScreen(navHostController)
        }
        composable(
            "TrackList/{id}",
            arguments = listOf(navArgument(TRACK_ID) { type = NavType.StringType })
        ) { backStack ->
            backStack.arguments?.getString(TRACK_ID)?.let { id ->
                ShowTrackList(navHostController, id)
            }
        }
        composable(
            "PlayTrackScreen?$TRACK_IMG_URL={imgUrl}&$TRACK_TITLE={title}&$TRACK_AUDIO_URL={audioUrl}",
            arguments = listOf(
                navArgument(TRACK_IMG_URL) { type = NavType.StringType },
                navArgument(TRACK_TITLE) { type = NavType.StringType },
                navArgument(TRACK_AUDIO_URL) { type = NavType.StringType })
        ) { backStack ->
            backStack.arguments?.let {
                val imgUrl = it.getString(TRACK_IMG_URL)
                val title = it.getString(TRACK_TITLE)
                val audioUrl = it.getString(TRACK_AUDIO_URL)
                val decodedImgUrl = Uri.decode(imgUrl)
                val decodedTitle = Uri.decode(title)
                val decodedAudioUrl = Uri.decode(audioUrl)
                PlayTrackScreen(navHostController, decodedImgUrl, decodedTitle, decodedAudioUrl)
            }
        }
    }
}