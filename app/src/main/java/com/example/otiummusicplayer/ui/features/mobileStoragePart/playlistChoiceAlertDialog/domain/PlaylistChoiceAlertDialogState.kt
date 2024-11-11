package com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import kotlinx.coroutines.flow.Flow

data class PlaylistChoiceAlertDialogState(
    val allPlayLists: Flow<PagingData<PlayerPlayListModel>>? = null,
    val selectedPlayList: PlayerPlayListModel? = null,
    val selectedTracks: List<TrackModel> = arrayListOf(),
)
