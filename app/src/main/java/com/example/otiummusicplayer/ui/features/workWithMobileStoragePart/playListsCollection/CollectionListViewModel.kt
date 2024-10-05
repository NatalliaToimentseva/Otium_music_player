package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain.CollectionListAction
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain.CollectionListState
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain.PlaylistErrors
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
            is CollectionListAction.AddPlaylist -> createPlayList(action.title)
            is CollectionListAction.DeletePlaylist -> deletePlayList()
            is CollectionListAction.GetAllPlaylists -> getPlayLists()
            is CollectionListAction.ShowDialog -> showDialog()
            is CollectionListAction.SetTitleFromDialog -> rememberTitleForNewPlaylist(action.title)
            is CollectionListAction.HideDialog -> hideDialog()
            is CollectionListAction.SelectItem -> selectItem(action.item)
            is CollectionListAction.ClearError -> clearError()
            is CollectionListAction.IsShowPermissionDialog -> showPermissionDialog(action.isShow)
        }
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

    private fun deletePlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            val listId = state.value.selectedItemsList.map { item -> item.id }
            deletePlaylistUseCase.deletePlayList(listId)
            state.tryEmit(state.value.copy(selectedItemsList = arrayListOf()))
        }
    }

    private fun getPlayLists() {
        state.tryEmit(
            state.value.copy(
                playLists = getPlaylistUseCase.getPlaylistsPage()
                    .cachedIn(viewModelScope)
            )
        )
    }

    private fun showDialog() {
        state.tryEmit(state.value.copy(isShowDialog = true, dialogText = ""))
    }

    private fun rememberTitleForNewPlaylist(text: String) {
        state.tryEmit(state.value.copy(dialogText = text))
    }

    private fun hideDialog() {
        state.tryEmit(state.value.copy(isShowDialog = false))
    }

    private fun selectItem(item: PlayerPlayListModel) {
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

    private fun showPermissionDialog(isShow: Boolean) {
        state.tryEmit(state.value.copy(showPermissionDialog = isShow))
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}