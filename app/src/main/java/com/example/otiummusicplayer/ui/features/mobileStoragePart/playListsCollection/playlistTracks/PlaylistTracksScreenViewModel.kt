package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks.domain.PlaylistTracksScreenAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks.domain.PlaylistTracksScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistTracksScreenViewModel @Inject constructor(
    private val loadPlaylistTracksUseCase: LoadPlaylistTracksUseCase,
    private val deleteTracksFromPlaylistUseCase: DeleteTracksFromPlaylistUseCase
) : ViewModel() {

    val state = MutableStateFlow(PlaylistTracksScreenState())

    fun processAction(action: PlaylistTracksScreenAction) {
        when (action) {
            is PlaylistTracksScreenAction.LoadPlaylistTracks -> loadPlaylistTracks(action.playlistId)
            is PlaylistTracksScreenAction.SelectTracks -> addTracksToSelected(action.track)
            is PlaylistTracksScreenAction.UnselectAllTracks -> unselectAllTracks()
            is PlaylistTracksScreenAction.DeleteTracksFromPlaylist -> deleteTracksFromPlaylist()
        }
    }

    private fun loadPlaylistTracks(playlistId: Long) {
        state.tryEmit(
            state.value.copy(
                playlistTracks = loadPlaylistTracksUseCase.loadTracks(playlistId)
                    .cachedIn(viewModelScope)
            )
        )
    }

    private fun addTracksToSelected(item: TrackModel) {
        val indexInList = state.value.playlistSelectedTracks.indexOfFirst { it.id == item.id }
        if (indexInList == -1) {
            state.tryEmit(
                state.value.copy(
                    playlistSelectedTracks = state.value.playlistSelectedTracks.plus(
                        item
                    )
                )
            )
        } else {
            state.tryEmit(
                state.value.copy(
                    playlistSelectedTracks = state.value.playlistSelectedTracks.minus(
                        item
                    )
                )
            )
        }
    }

    private fun unselectAllTracks() {
        state.tryEmit(state.value.copy(playlistSelectedTracks = arrayListOf()))
    }

    private fun deleteTracksFromPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            if (state.value.playlistSelectedTracks.isNotEmpty()) {
                val listOfSelectedTracksId = state.value.playlistSelectedTracks.map { tracks -> tracks.id }
                deleteTracksFromPlaylistUseCase.deleteTracks(listOfSelectedTracksId)
                state.tryEmit(state.value.copy(playlistSelectedTracks = arrayListOf()))
            }
        }
    }
}