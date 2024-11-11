package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.TrackModel
import kotlinx.coroutines.flow.Flow

data class PlaylistTracksScreenState(
    val playlistTracks: Flow<PagingData<TrackModel>>? = null,
    val playlistSelectedTracks: List<TrackModel> = arrayListOf(),
)
