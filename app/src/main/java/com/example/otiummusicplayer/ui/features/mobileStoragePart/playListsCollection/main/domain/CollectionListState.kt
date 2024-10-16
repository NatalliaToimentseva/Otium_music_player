package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import kotlinx.coroutines.flow.Flow

data class CollectionListState(
    val showPermissionDialog: Boolean = false,
    val playLists: Flow<PagingData<PlayerPlayListModel>>? = null,
    val showPlaylistDialog: Boolean = false,
    val showRenamePlaylistDialog: PlayerPlayListModel? = null,
    val dialogText: String = "",
    val selectedPlaylists: List<PlayerPlayListModel> = arrayListOf(),
    val error: String? = null,
)
