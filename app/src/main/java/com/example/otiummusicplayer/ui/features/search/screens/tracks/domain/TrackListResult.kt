package com.example.otiummusicplayer.ui.features.search.screens.tracks.domain

import com.example.otiummusicplayer.models.TrackModel

sealed class TrackListResult {

    data class Success(val data: List<TrackModel>) : TrackListResult()

    data class Error(val throwable: Throwable) : TrackListResult()

    data object Loading : TrackListResult()
}
