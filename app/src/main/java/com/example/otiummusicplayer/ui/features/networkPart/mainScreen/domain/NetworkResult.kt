package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain

import com.example.otiummusicplayer.models.networkPart.AlbumModel

sealed class NetworkResult {

    data class SuccessAlbumByArtist(val data: List<AlbumModel>) : NetworkResult()

    data class Error(val throwable: Throwable) : NetworkResult()

    data object Loading : NetworkResult()
}