package com.example.otiummusicplayer.ui.features.search.screens.tracks.domain

import com.example.otiummusicplayer.network.entities.TrackData

data class TrackLisState(
    val tracksData: TrackData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
