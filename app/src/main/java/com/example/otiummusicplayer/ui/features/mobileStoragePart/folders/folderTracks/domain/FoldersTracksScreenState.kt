package com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.TrackModel
import kotlinx.coroutines.flow.Flow

data class FoldersTracksScreenState(
    val showPermissionDialog: Boolean = false,
    val isLoading: Boolean = false,
    val tracksFolderList: Flow<PagingData<TrackModel>>? = null,
    val selectedTracks: List<TrackModel> = arrayListOf(),
    val isShowPlaylistsDialog: Boolean = false,
)
