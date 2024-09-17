package com.example.otiummusicplayer.ui.features.search.screens.main.domain

import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists

sealed class NetworkSearchResult {

    data class SuccessAlbumByArtist(val data: Album) : NetworkSearchResult()

    data class SuccessAlbum(val data: Album) : NetworkSearchResult()

    data class SuccessArtist(val data: Artists) : NetworkSearchResult()

    data class Error(val throwable: Throwable) : NetworkSearchResult()

    data object Loading : NetworkSearchResult()
}