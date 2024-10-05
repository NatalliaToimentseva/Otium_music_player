package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.folderTracks.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.networkPart.TrackModel
import kotlinx.coroutines.flow.Flow

data class FoldersTracksScreenState(
    val tracksFolderList: Flow<PagingData<TrackModel>>? = null,
    val selectedItems: List<TrackModel> = arrayListOf(),
    val isLoading: Boolean = false,
    val showPermissionDialog: Boolean = false
)
