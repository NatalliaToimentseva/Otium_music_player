package com.example.otiummusicplayer.ui.features.search.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkSearchResult
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkSearchScreenViewModel @Inject constructor(
    private val albumsUseCase: LoadAlbumsUseCase,
    private val artistUseCase: LoadArtistsUseCase,
    private val albumByArtistUseCase: LoadAlbumByArtistUseCase
) : ViewModel() {

    val state = MutableStateFlow(NetworkSearchState())

    fun processAction(action: NetworkSearchAction) {
        when (action) {
            NetworkSearchAction.LoadInitialData -> init()
            NetworkSearchAction.ShowDialog -> showDialog()
            NetworkSearchAction.HideDialog -> closeDialog()
            is NetworkSearchAction.LoadAlbumsByArtist -> getAlbumsByArtist(action.id)
            NetworkSearchAction.ClearError -> clearError()
        }
    }

    private fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            albumsUseCase.loadAlbum().collect { result ->
                handleResult(result)
            }
            artistUseCase.loadArtists().collect { result ->
                handleResult(result)
            }
//            val artists = api.getArtists()
//            state.tryEmit(state.value.copy(albums = albums, artists = artists))
        }
    }

    private fun showDialog() {
        state.tryEmit(state.value.copy(showDialog = true))
    }

    private fun closeDialog() {
        state.tryEmit(state.value.copy(showDialog = false))
    }

    private fun getAlbumsByArtist(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            albumByArtistUseCase.loadAlbumByArtist(id).collect { result ->
                handleResult(result)
                showDialog()
            }
//            state.tryEmit(state.value.copy(artistAlbums = api.getAlbumsByArtist()))
        }
    }

    private fun handleResult(result: NetworkSearchResult) {
        when (result) {
            NetworkSearchResult.Loading -> state.tryEmit(state.value.copy(isLoading = true))
            is NetworkSearchResult.Error -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    error = result.throwable.message
                )
            )

            is NetworkSearchResult.SuccessAlbum -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    albums = result.data
                )
            )

            is NetworkSearchResult.SuccessAlbumByArtist -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    artistAlbums = result.data
                )
            )

            is NetworkSearchResult.SuccessArtist -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    artists = result.data
                )
            )
        }
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}