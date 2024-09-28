package com.example.otiummusicplayer.ui.features.searchScreen.main.domain

sealed class NetworkSearchAction {

    data object LoadInitialData : NetworkSearchAction()

    data object HideDialog : NetworkSearchAction()

    data object ClearError : NetworkSearchAction()

    data class LoadAlbumsByArtist(val id: String) : NetworkSearchAction()

    data class SetSearchValue(val value: String) : NetworkSearchAction()

    data object SearchByQuery : NetworkSearchAction()

    data object ClearSearchResult : NetworkSearchAction()
}