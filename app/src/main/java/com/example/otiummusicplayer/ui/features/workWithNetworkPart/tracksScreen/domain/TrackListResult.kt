package com.example.otiummusicplayer.ui.features.workWithNetworkPart.tracksScreen.domain

import com.example.otiummusicplayer.models.networkPart.TrackModel

sealed class TrackListResult {

    data class Success(val data: List<TrackModel>) : TrackListResult()

    data class Error(val throwable: Throwable) : TrackListResult()

    data object Loading : TrackListResult()
}
