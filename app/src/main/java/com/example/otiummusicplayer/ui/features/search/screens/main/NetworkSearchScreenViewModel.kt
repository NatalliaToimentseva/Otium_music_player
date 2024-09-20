package com.example.otiummusicplayer.ui.features.search.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.repository.dataSource.AlbumPagingSource
import com.example.otiummusicplayer.repository.dataSource.ArtistPagingSource
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkResult
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkSearchState
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
    private val loadAlbumByArtistUseCase: LoadAlbumByArtistUseCase
) : ViewModel() {

    val state = MutableStateFlow(NetworkSearchState())

    init {
        processAction(NetworkSearchAction.LoadInitialData)
    }

    fun processAction(action: NetworkSearchAction) {
        when (action) {
            NetworkSearchAction.LoadInitialData -> init()
            NetworkSearchAction.HideDialog -> closeDialog()
            is NetworkSearchAction.LoadAlbumsByArtist -> getAlbumsByArtist(action.id)
            NetworkSearchAction.ClearError -> clearError()
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
                    isLoading = false
                )
            )
        }
    }

    private fun closeDialog() {
        state.tryEmit(state.value.copy(artistAlbums = null, showDialog = false))
    }

    private fun getAlbumsByArtist(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state.tryEmit(state.value.copy(isLoading = true))
            loadAlbumByArtistUseCase.loadAlbumsByArtist(id).collect { result ->
                when (result) {
                    is NetworkResult.Error -> state.tryEmit(
                        state.value.copy(
                            isLoading = false,
                            error = "Error of loading: ${result.throwable.message}"
                        )
                    )

                    is NetworkResult.Success<*> -> state.tryEmit(
                        state.value.copy(
                            isLoading = false,
                            artistAlbums = result.data as List<AlbumModel>,
                            showDialog = true
                        )
                    )
                }
            }
        }
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}