package com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.main.domain

import com.example.otiummusicplayer.models.mobilePart.TracksFolders

data class FoldersState(
    val folders: Set<TracksFolders>? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)
