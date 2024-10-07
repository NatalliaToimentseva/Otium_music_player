package com.example.otiummusicplayer.ui.navigation

sealed class Route(val route: String) {

    data object PlaylistsScreen : Route("PlaylistsScreen")

    data object PlaylistTracksScreen : Route("PlaylistTracksScreen/{playlistId}") {

        fun selectRoute(playlistId: Long): String {
            return "PlaylistTracksScreen/$playlistId"
        }
    }

    data object FoldersScreen : Route("FoldersScreen")

    data object MobileStorageTracksScreen : Route("MobileStorageTracksScreen")

    data object FoldersTracksScreen : Route("FoldersTracksScreen/{folderId}") {

        fun selectRoute(folderId: Int): String {
            return "FoldersTracksScreen/$folderId"
        }
    }

    data object NetworkSearch : Route("NetworkSearchScreen")

    data object TrackList : Route("TrackList/{id}") {

        fun selectRoute(id: String): String {
            return "TrackList/$id"
        }
    }

    data object PlayTrackScreen : Route("PlayTrackScreen?whereFrom={whereFrom}&itemId={itemId}&tracks={tracks}") {

        fun selectRoute(whereFrom: String, itemId: String, tracks: String): String {
            return "PlayTrackScreen?whereFrom=$whereFrom&itemId=$itemId&tracks=$tracks"
        }
    }
}