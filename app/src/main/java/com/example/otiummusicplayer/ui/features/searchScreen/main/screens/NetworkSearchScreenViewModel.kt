package com.example.otiummusicplayer.ui.features.searchScreen.main.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.otiummusicplayer.repository.dataSource.AlbumPagingSource
import com.example.otiummusicplayer.repository.dataSource.ArtistPagingSource
import com.example.otiummusicplayer.repository.dataSource.SearchPagingSource
import com.example.otiummusicplayer.ui.features.searchScreen.main.LoadAlbumByArtistUseCase
import com.example.otiummusicplayer.ui.features.searchScreen.main.LoadFavoriteDataUseCase
import com.example.otiummusicplayer.ui.features.searchScreen.main.domain.NetworkResult
import com.example.otiummusicplayer.ui.features.searchScreen.main.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.searchScreen.main.domain.NetworkSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val INIT_PAGE_SIZE = 10
private const val INIT_LOAD_SIZE = 30

@HiltViewModel
class NetworkSearchScreenViewModel @Inject constructor(
    private val albumPagingSource: AlbumPagingSource,
    private val artistPagingSource: ArtistPagingSource,
    private val loadAlbumByArtistUseCase: LoadAlbumByArtistUseCase,
    private val searchPagingSource: SearchPagingSource.Factory,
    private val loadFavoriteDataUseCase: LoadFavoriteDataUseCase
) : ViewModel() {

    val state = MutableStateFlow(NetworkSearchState())

    init {
        processAction(NetworkSearchAction.LoadInitialData)
    }

    fun processAction(action: NetworkSearchAction) {
        when (action) {
            NetworkSearchAction.LoadInitialData -> init()
            NetworkSearchAction.HideDialog -> closeDialog()
            NetworkSearchAction.ClearError -> clearError()
            NetworkSearchAction.ClearSearchResult -> clearSearchData()
            is NetworkSearchAction.LoadAlbumsByArtist -> getAlbumsByArtist(action.id)
            is NetworkSearchAction.SearchByQuery -> searchTracksByQuery()
            is NetworkSearchAction.SetSearchValue -> setSearchValue(action.value)
        }
    }

    private fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            state.tryEmit(state.value.copy(isLoading = true))
            state.tryEmit(
                state.value.copy(
                    artists = Pager(
                        PagingConfig(pageSize = INIT_PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)
                    ) {
                        artistPagingSource
                    }.flow
                        .cachedIn(viewModelScope),
                    albums = Pager(
                        PagingConfig(pageSize = INIT_PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)
                    ) {
                        albumPagingSource
                    }.flow
                        .cachedIn(viewModelScope),
                    favoriteList = loadFavoriteDataUseCase.loadFavoriteListPage()
                        .cachedIn(viewModelScope),
                    isLoading = false
                )
            )
        }
    }

    private fun setSearchValue(value: String) {
        state.tryEmit(state.value.copy(searchValue = value, isSearchError = false))
    }

    private fun getAlbumsByArtist(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state.tryEmit(state.value.copy(isLoading = true))
            loadAlbumByArtistUseCase.loadAlbumsByArtist(id).collect { result ->
                handleResult(result)
            }
        }
    }

    private fun searchTracksByQuery() {
        if (state.value.searchValue.isBlank()) {
            state.tryEmit(state.value.copy(isSearchError = true))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                state.tryEmit(
                    state.value.copy(
                        searchResult = Pager(
                            PagingConfig(
                                pageSize = INIT_PAGE_SIZE,
                                initialLoadSize = INIT_LOAD_SIZE
                            )
                        ) {
                            searchPagingSource.create(state.value.searchValue)
                        }.flow
                            .cachedIn(viewModelScope)
                    )
                )
            }
        }
    }

    private fun clearSearchData() {
        state.tryEmit(state.value.copy(searchResult = null))
    }

    private fun closeDialog() {
        state.tryEmit(state.value.copy(artistAlbums = null, showDialog = false))
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }

    private fun handleResult(result: NetworkResult) {
        when (result) {
            is NetworkResult.Error -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    error = "Error of loading: ${result.throwable.message}"
                )
            )

            NetworkResult.Loading -> state.tryEmit(state.value.copy(isLoading = true))
            is NetworkResult.SuccessAlbumByArtist -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    artistAlbums = result.data,
                    showDialog = true
                )
            )
        }
    }
}