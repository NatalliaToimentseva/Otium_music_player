package com.example.otiummusicplayer.ui.navigation

sealed class Route(val route: String) {

    data object PlaylistsScreen : Route("PlaylistsScreen")

    data object FoldersScreen : Route("FoldersScreen")

    data object MobileStorageTracksScreen : Route("MobileStorageTracksScreen")

    data object NetworkSearch : Route("NetworkSearchScreen")

    data object TrackList : Route("TrackList/{id}") {

        fun selectRoute(id: String): String {
            return "TrackList/$id"
        }
    }

    data object PlayTrackScreen : Route("PlayTrackScreen?itemId={itemId}&tracks={tracks}") {

        fun selectRoute(itemId: String, tracks: String): String {
            return "PlayTrackScreen?itemId=$itemId&tracks=$tracks"
        }
    }
}