package com.example.otiummusicplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.otiummusicplayer.ui.features.playTrack.PlayTrackDestination
import com.example.otiummusicplayer.ui.features.search.screens.main.NetworkSearchDestination
import com.example.otiummusicplayer.ui.features.search.screens.tracks.TrackListDestination

private const val TRACK_ID = "id"
private const val TRACK_ITEM_ID = "itemId"

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
                navArgument(TRACK_ID) { type = NavType.IntType },
                navArgument(TRACK_ITEM_ID) {type = NavType.StringType}
            )
        ) { backStack ->
            backStack.arguments?.run {
                val id = getInt(TRACK_ID)
                val itemId = getString(TRACK_ITEM_ID)
                if (itemId != null) {
                    PlayTrackDestination(id, itemId, navHostController)
                }
            }
        }
    }
}