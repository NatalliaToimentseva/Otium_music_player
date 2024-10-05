package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.networkPart.TrackModel
import kotlinx.coroutines.flow.Flow

data class MobileStorageTracksState(
    val storageTracks: Flow<PagingData<TrackModel>>? = null,
    val selectedItems: List<TrackModel> = arrayListOf(),
    val isLoading: Boolean = false,
    val showPermissionDialog: Boolean = false
)
