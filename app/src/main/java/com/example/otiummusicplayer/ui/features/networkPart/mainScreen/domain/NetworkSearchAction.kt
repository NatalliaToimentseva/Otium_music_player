package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain

sealed class NetworkSearchAction {

    data object LoadInitialData : NetworkSearchAction()

    data class LoadAlbumsByArtist(val id: String) : NetworkSearchAction()

    data class SetSearchValue(val value: String) : NetworkSearchAction()

    data object SearchByQuery : NetworkSearchAction()

    data object ClearSearchResult : NetworkSearchAction()

    data object HideDialog : NetworkSearchAction()

    data object ClearError : NetworkSearchAction()
}