package com.example.otiummusicplayer.ui.features.search.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.ui.features.search.tracks.domain.TrackLisState
import com.example.otiummusicplayer.ui.features.search.tracks.domain.TrackListAction
import com.example.otiummusicplayer.ui.features.search.tracks.domain.TrackListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackListViewModel @Inject constructor(
    private val useCase: LoadTracksByAlbumIdUseCase
) : ViewModel() {

    val state = MutableStateFlow(TrackLisState())

    fun processAction(action: TrackListAction) {
        when (action) {
            is TrackListAction.Init -> getTracksByAlbums(action.id)
            TrackListAction.ClearError -> clearError()
        }
    }

    private fun getTracksByAlbums(id: String) {
        if(state.value.tracks == null) {
            viewModelScope.launch(Dispatchers.IO) {
                useCase.loadTracks(id.toInt()).collect { result ->
                    handleResult(result)
                }
            }
        }
    }

    private fun handleResult(result: TrackListResult) {
        when (result) {
            TrackListResult.Loading -> state.tryEmit(state.value.copy(isLoading = true))
            is TrackListResult.Error -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    error = "Error of loading: ${result.throwable.message}"
                )
            )
            is TrackListResult.Success -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    tracks = result.data
                )
            )
        }
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}
