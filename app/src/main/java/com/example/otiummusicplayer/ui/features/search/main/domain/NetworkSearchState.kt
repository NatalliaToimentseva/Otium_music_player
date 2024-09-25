package com.example.otiummusicplayer.ui.features.search.main.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.models.ArtistModel
import com.example.otiummusicplayer.models.TrackModel
import kotlinx.coroutines.flow.Flow

const val EMPTY = ""

data class NetworkSearchState(
    val artists: Flow<PagingData<ArtistModel>>? = null,
    val albums: Flow<PagingData<AlbumModel>>? = null,
    val artistAlbums: List<AlbumModel>? = null,
    val searchValue: String = EMPTY,
    val searchResult: Flow<PagingData<TrackModel>>? = null,
    val isSearchError: Boolean = false,
    val showDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)