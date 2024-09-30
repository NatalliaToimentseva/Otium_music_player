package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import kotlinx.coroutines.flow.Flow

data class CollectionListState(
    val playLists: Flow<PagingData<PlayerPlayListModel>>? = null,
    val isShowDialog: Boolean = false,
    val dialogText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
