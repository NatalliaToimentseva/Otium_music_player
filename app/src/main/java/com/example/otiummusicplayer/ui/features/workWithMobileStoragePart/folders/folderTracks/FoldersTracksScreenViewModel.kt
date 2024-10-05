package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.folderTracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.networkPart.TrackModel
import com.example.otiummusicplayer.repository.dataSource.MobileStorageFolderTracksPagingSource
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.folderTracks.domain.FoldersTracksScreenAction
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.folderTracks.domain.FoldersTracksScreenState
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
            is FoldersTracksScreenAction.IsShowPermissionDialog -> showPermissionDialog(action.isShow)
            is FoldersTracksScreenAction.LoadFolderTracks -> loadFolderTracks(action.folderId)
            is FoldersTracksScreenAction.AddItemToSelected -> addItemToSelected(action.item)
            FoldersTracksScreenAction.AddTrackToPlayList -> addTrackToPlayList()
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

    private fun addTrackToPlayList() {

    }

    private fun addItemToSelected(item: TrackModel) {
        val indexInList = state.value.selectedItems.indexOfFirst { it.id == item.id}
        if (indexInList == -1) {
            state.tryEmit(
                state.value.copy(
                    selectedItems = state.value.selectedItems.plus(
                        item
                    )
                )
            )
        } else {
            state.tryEmit(
                state.value.copy(
                    selectedItems = state.value.selectedItems.minus(
                        item
                    )
                )
            )
        }
    }

    private fun showPermissionDialog(isShow: Boolean) {
        state.tryEmit(state.value.copy(showPermissionDialog = isShow))
    }
}