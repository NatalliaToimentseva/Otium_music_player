package com.example.otiummusicplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.otiummusicplayer.ui.features.playerScreen.PlayTrackDestination
import com.example.otiummusicplayer.ui.features.searchScreen.main.screens.NetworkSearchDestination
import com.example.otiummusicplayer.ui.features.searchScreen.tracks.TrackListDestination

private const val TRACK_ID = "id"
private const val TRACK_ITEM_ID = "itemId"
private const val TRACKS_LIST = "tracks"

@Composable
fun AppGraph(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = Route.NetworkSearch.route) {
        composable(Route.NetworkSearch.route) {
            NetworkSearchDestination(navHostController)
        }
        composable(
            Route.TrackList.route,
            arguments = listOf(navArgument(TRACK_ID) { type = NavType.StringType })
        ) { backStack ->
            backStack.arguments?.getString(TRACK_ID)?.let { id ->
                TrackListDestination(id, navHostController)
            }
        }
        composable(
            Route.PlayTrackScreen.route,
            arguments = listOf(
                navArgument(TRACK_ITEM_ID) { type = NavType.StringType },
                navArgument(TRACKS_LIST) { type = NavType.StringType}
            )
        ) { backStack ->
            backStack.arguments?.run {
                val itemId = getString(TRACK_ITEM_ID)
                val tracks = getString(TRACKS_LIST, "")

                if (itemId != null && tracks != null) {
                    PlayTrackDestination(itemId = itemId, tracks = tracks, navHostController)
                }
            }
        }
    }
}