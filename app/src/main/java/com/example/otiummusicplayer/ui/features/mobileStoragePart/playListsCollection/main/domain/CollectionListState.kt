package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import kotlinx.coroutines.flow.Flow

data class CollectionListState(
    val showPermissionDialog: Boolean = false,
    val playLists: Flow<PagingData<PlayerPlayListModel>>? = null,
    val isShowDialog: Boolean = false,
    val dialogText: String = "",
    val selectedItemsList: List<PlayerPlayListModel> = arrayListOf(),
    val error: String? = null,
)
