package com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import com.example.otiummusicplayer.ui.features.mobileStoragePart.generalUseCases.AddTracksToPlaylistUseCase
import com.example.otiummusicplayer.ui.features.mobileStoragePart.generalUseCases.GetPlaylistUseCase
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog.domain.PlaylistChoiceAlertDialogAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog.domain.PlaylistChoiceAlertDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistChoiceAlertDialogViewModel @Inject constructor(
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val addTracksToPlaylistUseCase: AddTracksToPlaylistUseCase
) : ViewModel() {
    val state = MutableStateFlow(PlaylistChoiceAlertDialogState())

    init {
        processAction(PlaylistChoiceAlertDialogAction.LoadPlaylists)
    }

    fun processAction(action: PlaylistChoiceAlertDialogAction) {
        when (action) {
            is PlaylistChoiceAlertDialogAction.LoadPlaylists -> loadPlayLists()
            is PlaylistChoiceAlertDialogAction.SetSelectedTracks -> setSelectedTracks(action.selectedTracks)
            is PlaylistChoiceAlertDialogAction.SelectPlayListFromDialog -> selectPlayListFromDialog(
                action.list
            )

            is PlaylistChoiceAlertDialogAction.AddTracksToPlayList -> addTrackToPlayList()
        }
    }

    private fun loadPlayLists() {
        if (state.value.allPlayLists == null) {
            state.tryEmit(
                state.value.copy(
                    allPlayLists = getPlaylistUseCase.getPlaylistsPage()
                        .cachedIn(viewModelScope)
                )
            )
        }
    }

    private fun setSelectedTracks(selectedTracks: List<TrackModel>) {
        state.tryEmit(state.value.copy(selectedTracks = selectedTracks))
    }

    private fun selectPlayListFromDialog(playList: PlayerPlayListModel) {
        state.tryEmit(state.value.copy(selectedPlayList = playList))
    }

    private fun addTrackToPlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value.selectedPlayList?.id?.let {
                addTracksToPlaylistUseCase.addTracksToPlaylist(
                    state.value.selectedTracks,
                    it
                )
            }
        }
    }
}