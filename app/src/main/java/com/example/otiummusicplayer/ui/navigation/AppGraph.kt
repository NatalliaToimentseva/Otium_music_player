package com.example.otiummusicplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.otiummusicplayer.ui.features.playerControlScreen.PlayTrackDestination
import com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks.MobileStorageTracksScreenDestination
import com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks.FoldersTracksScreenDestination
import com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.main.FoldersScreenDestination
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.CollectionListDestination
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks.PlaylistTracksScreenDestination
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.NetworkSearchDestination
import com.example.otiummusicplayer.ui.features.networkPart.tracksScreen.TrackListDestination

private const val TRACK_ID = "id"
private const val FOLDER_ID = "folderId"
private const val PLAYLIST_ID = "playlistId"
private const val TRACK_ITEM_ID = "itemId"
private const val TRACKS_LIST = "tracks"
private const val WHERE_FROM = "whereFrom"

@Composable
fun AppGraph(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = Route.PlaylistsScreen.route) {
        composable(Route.PlaylistsScreen.route) {
            CollectionListDestination(navHostController)
        }
        composable(
            Route.PlaylistTracksScreen.route,
            arguments = listOf(navArgument(PLAYLIST_ID) { type = NavType.LongType })
        ) { backStack ->
            backStack.arguments?.getLong(PLAYLIST_ID)?.let { playlistId ->
                PlaylistTracksScreenDestination(playlistId, navHostController)
            }
        }
        composable(Route.FoldersScreen.route) {
            FoldersScreenDestination(navHostController)
        }
        composable(
            Route.FoldersTracksScreen.route,
            arguments = listOf(navArgument(FOLDER_ID) { type = NavType.IntType })
        ) { backStack ->
            backStack.arguments?.getInt(FOLDER_ID)?.let { folderId ->
                FoldersTracksScreenDestination(folderId, navHostController)
            }
        }
        composable(Route.MobileStorageTracksScreen.route) {
            MobileStorageTracksScreenDestination(navHostController)
        }
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
                navArgument(WHERE_FROM) { type = NavType.StringType },
                navArgument(TRACK_ITEM_ID) { type = NavType.StringType },
                navArgument(TRACKS_LIST) { type = NavType.StringType }
            )
        ) { backStack ->
            backStack.arguments?.run {
                val whereFrom = getString(WHERE_FROM)
                val itemId = getString(TRACK_ITEM_ID)
                val tracks = getString(TRACKS_LIST, "")

                if (itemId != null && tracks != null && whereFrom != null) {
                    PlayTrackDestination(
                        whereFrom = whereFrom,
                        itemId = itemId,
                        tracks = tracks,
                        navHostController
                    )
                }
            }
        }
    }
}