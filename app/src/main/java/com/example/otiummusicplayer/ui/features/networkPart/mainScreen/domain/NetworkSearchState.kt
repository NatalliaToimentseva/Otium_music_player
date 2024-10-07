package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain

import androidx.paging.PagingData
import com.example.otiummusicplayer.models.networkPart.AlbumModel
import com.example.otiummusicplayer.models.networkPart.ArtistModel
import com.example.otiummusicplayer.models.TrackModel
import kotlinx.coroutines.flow.Flow

const val EMPTY = ""
const val ALL_TAB = "All"
const val FAVORITE_TAB = "Favorite"

data class NetworkSearchState(
    val tabsName: List<String> = listOf(ALL_TAB, FAVORITE_TAB),
    val initialPage: Int = 0,
    val artists: Flow<PagingData<ArtistModel>>? = null,
    val albums: Flow<PagingData<AlbumModel>>? = null,
    val artistAlbums: List<AlbumModel>? = null,
    val favoriteList:Flow<PagingData<TrackModel>>? = null,
    val searchResult: Flow<PagingData<TrackModel>>? = null,
    val searchValue: String = EMPTY,
    val isSearchError: Boolean = false,
    val showDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)