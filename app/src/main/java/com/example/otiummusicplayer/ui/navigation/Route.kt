package com.example.otiummusicplayer.ui.navigation

sealed class Route(val route: String) {

    data object NetworkSearch : Route("NetworkSearchScreen")

    data object TrackList : Route("TrackList/{id}") {

        fun selectRoute(id: String): String {
            return "TrackList/$id"
        }
    }

    data object PlayTrackScreen : Route("PlayTrackScreen/{id}/{itemId}") {

        fun selectRoute(id: Int, itemId: String): String {
            return "PlayTrackScreen/$id/$itemId"
        }
    }
}