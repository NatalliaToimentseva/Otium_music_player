package com.example.otiummusicplayer.ui.features.search.main.domain

import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.models.TrackModel


sealed class NetworkResult {

//    data class Success<T: Any>(val data: List<T>) : NetworkResult()

    data class SuccessAlbumByArtist(val data: List<AlbumModel>) : NetworkResult()

//    data class SuccessSearchTracks(val data: List<TrackModel>) : NetworkResult()

    data class Error(val throwable: Throwable) : NetworkResult()

    data object Loading : NetworkResult()
}