package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.domain

import com.example.otiummusicplayer.models.mobilePart.TracksFolders

sealed class FoldersResult {

    data class Success(val data: Set<TracksFolders>) : FoldersResult()

    data class Error(val message: String) : FoldersResult()

    data object Loading : FoldersResult()
}