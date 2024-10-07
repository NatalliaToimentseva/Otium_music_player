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
    private val deletePlaylistUseCase: DeletePlaylistUseCase
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
            is CollectionListAction.ShowDialog -> showDialog()
            is CollectionListAction.SetNewPlaylistTitleFromDialog -> rememberTitleForNewPlaylist(
                action.title
            )

            is CollectionListAction.AddPlaylist -> createPlayList(action.title)
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
        val indexInList = state.value.selectedItemsList.indexOfFirst { it.id == item.id }
        if (indexInList == -1) {
            state.tryEmit(
                state.value.copy(
                    selectedItemsList = state.value.selectedItemsList.plus(
                        item
                    )
                )
            )
        } else {
            state.tryEmit(
                state.value.copy(
                    selectedItemsList = state.value.selectedItemsList.minus(
                        item
                    )
                )
            )
        }
    }

    private fun unselectAll() {
        state.tryEmit(state.value.copy(selectedItemsList = arrayListOf()))
    }

    private fun deletePlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            if (state.value.selectedItemsList.isNotEmpty()) {
                val listId = state.value.selectedItemsList.map { item -> item.id }
                deletePlaylistUseCase.deletePlayList(listId)
                state.tryEmit(state.value.copy(selectedItemsList = arrayListOf()))
            }
        }
    }

    private fun showDialog() {
        state.tryEmit(state.value.copy(isShowDialog = true, dialogText = ""))
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

    private fun hideDialog() {
        state.tryEmit(state.value.copy(isShowDialog = false))
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}