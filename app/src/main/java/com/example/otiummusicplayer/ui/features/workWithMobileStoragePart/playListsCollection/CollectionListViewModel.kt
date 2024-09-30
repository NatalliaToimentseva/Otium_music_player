package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain.CollectionListAction
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain.CollectionListState
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
            is CollectionListAction.DeletePlaylist -> deletePlayList(action.id)
            CollectionListAction.GetAllPlaylists -> getPlayLists()
            CollectionListAction.ShowDialog -> showDialog()
            is CollectionListAction.SetTitleFromDialog -> rememberTitleForNewPlaylist(action.title)
            CollectionListAction.HideDialog -> hideDialog()
            CollectionListAction.ClearError -> clearError()
        }
    }

    private fun createPlayList(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlayListUseCase.addPlayList(PlayerPlayListModel(0, title))
        }
    }

    private fun deletePlayList(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePlaylistUseCase.deletePlayList(id)
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
        state.tryEmit(state.value.copy(isShowDialog = true))
    }

    private fun rememberTitleForNewPlaylist(text: String) {
        state.tryEmit(state.value.copy(dialogText = text))
    }

    private fun hideDialog() {
        state.tryEmit(state.value.copy(isShowDialog = false))
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}