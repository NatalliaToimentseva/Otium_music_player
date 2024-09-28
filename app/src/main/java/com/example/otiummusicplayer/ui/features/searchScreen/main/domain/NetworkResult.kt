package com.example.otiummusicplayer.ui.features.searchScreen.main.domain

import com.example.otiummusicplayer.models.AlbumModel

sealed class NetworkResult {

    data class SuccessAlbumByArtist(val data: List<AlbumModel>) : NetworkResult()

    data class Error(val throwable: Throwable) : NetworkResult()

    data object Loading : NetworkResult()
}