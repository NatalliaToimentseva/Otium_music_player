package com.example.otiummusicplayer.ui.features.workWithNetworkPart.tracksScreen.domain

import com.example.otiummusicplayer.models.networkPart.TrackModel

data class TrackLisState(
    val tracks: List<TrackModel>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
