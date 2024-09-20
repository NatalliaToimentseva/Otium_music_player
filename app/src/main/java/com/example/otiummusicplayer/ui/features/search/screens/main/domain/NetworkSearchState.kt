package com.example.otiummusicplayer.ui.features.search.screens.main.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.models.ArtistModel
import kotlinx.coroutines.flow.Flow

data class NetworkSearchState(
    val artists: Flow<PagingData<ArtistModel>>? = null,
    val albums: Flow<PagingData<AlbumModel>>? = null,
    val artistAlbums: List<AlbumModel>? = null,
    val showDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)