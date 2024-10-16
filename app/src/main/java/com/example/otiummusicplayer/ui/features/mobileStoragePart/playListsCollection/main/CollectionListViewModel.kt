package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import com.example.otiummusicplayer.ui.features.mobileStoragePart.generalUseCases.GetPlaylistUseCase
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain.CollectionListAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain.CollectionListState
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain.PlaylistErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionListViewModel @Inject constructor(
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val addPlayListUseCase: AddPlayListUseCase,
    private val renamePlaylistUseCase: RenamePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val deleteTracksWithPlaylistUseCase: DeleteTracksWithPlaylistUseCase
) : ViewModel() {

    val state = MutableStateFlow(CollectionListState())

    init {
        processAction(CollectionListAction.GetAllPlaylists)
    }

    fun processAction(action: CollectionListAction) {
        when (action) {
            is CollectionListAction.IsShowPermissionDialog -> showPermissionDialog(action.isShow)
            is CollectionListAction.GetAllPlaylists -> getPlayLists()
            is CollectionListAction.SelectPlaylist -> selectPlaylist(action.item)
            is CollectionListAction.UnselectAll -> unselectAll()
            is CollectionListAction.DeletePlaylist -> deletePlayList()
            is CollectionListAction.ShowPlaylistDialog -> showDialog(action.isShow)
            is CollectionListAction.ShowRenamePlaylistDialog -> showRenamePlaylistDialog(action.playlist)
            is CollectionListAction.SetNewPlaylistTitleFromDialog -> rememberTitleForNewPlaylist(
                action.title
            )
            is CollectionListAction.AddPlaylist -> createPlayList(action.title)
            is CollectionListAction.RenamePlaylist -> renamePlayList(
                action.playlist,
                action.newTitle
            )

            is CollectionListAction.HideDialog -> hideDialog()
            is CollectionListAction.ClearError -> clearError()
        }
    }

    private fun showPermissionDialog(isShow: Boolean) {
        state.tryEmit(state.value.copy(showPermissionDialog = isShow))
    }

    private fun getPlayLists() {
        state.tryEmit(
            state.value.copy(
                playLists = getPlaylistUseCase.getPlaylistsPage()
                    .cachedIn(viewModelScope)
            )
        )
    }

    private fun selectPlaylist(item: PlayerPlayListModel) {
        val indexInList = state.value.selectedPlaylists.indexOfFirst { it.id == item.id }
        if (indexInList == -1) {
            state.tryEmit(
                state.value.copy(
                    selectedPlaylists = state.value.selectedPlaylists.plus(
                        item
                    )
                )
            )
        } else {
            state.tryEmit(
                state.value.copy(
                    selectedPlaylists = state.value.selectedPlaylists.minus(
                        item
                    )
                )
            )
        }
    }

    private fun unselectAll() {
        state.tryEmit(state.value.copy(selectedPlaylists = arrayListOf()))
    }

    private fun deletePlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            if (state.value.selectedPlaylists.isNotEmpty()) {
                val listId = state.value.selectedPlaylists.map { item -> item.id }
                deletePlaylistUseCase.deletePlayList(listId)
                deleteTracksWithPlaylistUseCase.deleteWithPlaylist(listId)
                state.tryEmit(state.value.copy(selectedPlaylists = arrayListOf()))
            }
        }
    }

    private fun showDialog(isShowDialog: Boolean) {
        state.tryEmit(state.value.copy(showPlaylistDialog = isShowDialog, dialogText = ""))
    }

    private fun showRenamePlaylistDialog(playlist: PlayerPlayListModel?) {
        state.tryEmit(state.value.copy(showRenamePlaylistDialog = playlist, dialogText = ""))
    }

    private fun rememberTitleForNewPlaylist(text: String) {
        state.tryEmit(state.value.copy(dialogText = text))
    }

    private fun createPlayList(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlayListUseCase.addPlayList(PlayerPlayListModel(0, title)).let { result ->
                if (result is PlaylistErrors.Error) {
                    state.tryEmit(state.value.copy(error = result.message))
                }
            }
        }
    }

    private fun renamePlayList(playlist: PlayerPlayListModel, newTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            renamePlaylistUseCase.renamePlayList(playlist.copy(playListTitle = newTitle))
                .let { result ->
                    if (result is PlaylistErrors.Error) {
                        state.tryEmit(state.value.copy(error = result.message))
                    }
                }
            state.tryEmit(
                state.value.copy(
                    selectedPlaylists = state.value.selectedPlaylists.minus(
                        playlist
                    )
                )
            )
        }
    }

    private fun hideDialog() {
        state.tryEmit(state.value.copy(showPlaylistDialog = false, showRenamePlaylistDialog = null))
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}