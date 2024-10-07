package com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.TrackModel
import kotlinx.coroutines.flow.Flow

data class MobileStorageTracksState(
    val showPermissionDialog: Boolean = false,
    val isLoading: Boolean = false,
    val storageTracks: Flow<PagingData<TrackModel>>? = null,
    val selectedTracks: List<TrackModel> = arrayListOf(),
    val isShowPlaylistsDialog: Boolean = false
)
