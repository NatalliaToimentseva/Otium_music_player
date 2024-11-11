package com.example.otiummusicplayer.ui.features.networkPart.tracksScreen.domain

import com.example.otiummusicplayer.models.TrackModel

data class TrackLisState(
    val tracks: List<TrackModel>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
