package com.example.otiummusicplayer.ui.features.search.screens.main.domain

import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackListAction

sealed class NetworkSearchAction {

    data object LoadInitialData : NetworkSearchAction()

    data object HideDialog : NetworkSearchAction()

    data object ClearError : NetworkSearchAction()

    data class LoadAlbumsByArtist(val id: String) : NetworkSearchAction()
}