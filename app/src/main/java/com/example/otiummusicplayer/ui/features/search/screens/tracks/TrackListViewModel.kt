package com.example.otiummusicplayer.ui.features.search.screens.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackLisState
import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackListAction
import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackListResult
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
            is TrackListAction.Init -> getTrByAlb(action.id)
            TrackListAction.ClearError -> clearError()
        }
    }

    private fun getTrByAlb(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.loadTracks(id).collect { result ->
                handleResult(result)

            }
//            val trackResponse = api.getTracksByAlbum()
//            val listTrackData = trackResponse.results.firstOrNull()
//            state.tryEmit(state.value.copy(tracksData = listTrackData))
        }
    }

    private fun handleResult(result: TrackListResult) {
        when (result) {
            TrackListResult.Loading -> state.tryEmit(state.value.copy(isLoading = true))
            is TrackListResult.Error -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    error = result.throwable.message
                )
            )
            is TrackListResult.Success -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    tracksData = result.data
                )
            )
        }
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}
