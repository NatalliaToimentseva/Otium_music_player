package com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.repository.dataSource.impl.MobileStorageFolderTracksPagingSource
import com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks.domain.FoldersTracksScreenAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks.domain.FoldersTracksScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val INIT_PAGE_SIZE = 10
private const val INIT_LOAD_SIZE = 20

@HiltViewModel
class FoldersTracksScreenViewModel @Inject constructor(
    private val mobileStorageFolderTracksPagingSource: MobileStorageFolderTracksPagingSource.Factory
) : ViewModel() {

    val state = MutableStateFlow(FoldersTracksScreenState())

    fun processAction(action: FoldersTracksScreenAction) {
        when (action) {
            is FoldersTracksScreenAction.LoadFolderTracks -> loadFolderTracks(action.folderId)
            is FoldersTracksScreenAction.IsShowPermissionDialog -> showPermissionDialog(action.isShow)
            is FoldersTracksScreenAction.AddTrackToSelected -> addTracksToSelected(action.item)
            is FoldersTracksScreenAction.UnselectAllTracks -> unselectAllTracks()
            is FoldersTracksScreenAction.ShowPlayListDialog -> showPlayListDialog()
            is FoldersTracksScreenAction.ClosePlayListDialog -> hidePlayListDialog()
        }
    }

    private fun loadFolderTracks(folderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            state.tryEmit(
                state.value.copy(
                    tracksFolderList = Pager(
                        PagingConfig(
                            pageSize = INIT_PAGE_SIZE,
                            initialLoadSize = INIT_LOAD_SIZE
                        )
                    ) {
                        mobileStorageFolderTracksPagingSource.create(folderId)
                    }.flow
                        .cachedIn(viewModelScope)
                )
            )
        }
    }

    private fun showPermissionDialog(isShow: Boolean) {
        state.tryEmit(state.value.copy(showPermissionDialog = isShow))
    }

    private fun addTracksToSelected(item: TrackModel) {
        val indexInList = state.value.selectedTracks.indexOfFirst { it.id == item.id }
        if (indexInList == -1) {
            state.tryEmit(
                state.value.copy(
                    selectedTracks = state.value.selectedTracks.plus(
                        item
                    )
                )
            )
        } else {
            state.tryEmit(
                state.value.copy(
                    selectedTracks = state.value.selectedTracks.minus(
                        item
                    )
                )
            )
        }
    }

    private fun unselectAllTracks() {
        state.tryEmit(state.value.copy(selectedTracks = arrayListOf()))
    }

    private fun showPlayListDialog() {
        state.tryEmit(state.value.copy(isShowPlaylistsDialog = true))
    }

    private fun hidePlayListDialog() {
        state.tryEmit(state.value.copy(isShowPlaylistsDialog = false))
    }
}