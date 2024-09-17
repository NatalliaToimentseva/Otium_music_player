package com.example.otiummusicplayer.ui.features.search.screens.tracks.domain

import com.example.otiummusicplayer.network.entities.TrackData

sealed class TrackListResult {

    data class Success(val data: TrackData?) : TrackListResult()

    data class Error(val throwable: Throwable) : TrackListResult()

    data object Loading : TrackListResult()
}
