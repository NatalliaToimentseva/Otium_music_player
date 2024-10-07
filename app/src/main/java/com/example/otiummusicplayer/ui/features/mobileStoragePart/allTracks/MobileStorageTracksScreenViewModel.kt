package com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.repository.dataSource.impl.MobileStorageTracksPagingSource
import com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks.domain.MobileStorageTracksAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks.domain.MobileStorageTracksState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

private const val INIT_PAGE_SIZE = 10
private const val INIT_LOAD_SIZE = 20

@HiltViewModel
class MobileStorageTracksScreenViewModel @Inject constructor(
    private val mobileStorageTracksPagingSource: MobileStorageTracksPagingSource
) : ViewModel() {

    val state = MutableStateFlow(MobileStorageTracksState())

    init {
        processAction(MobileStorageTracksAction.LoadStorageTracks)
    }

    fun processAction(action: MobileStorageTracksAction) {
        when (action) {
            is MobileStorageTracksAction.LoadStorageTracks -> loadMobileStorageTracks()
            is MobileStorageTracksAction.IsShowPermissionDialog -> showPermissionDialog(action.isShow)
            is MobileStorageTracksAction.AddTrackToSelected -> addTracksToSelected(action.item)
            is MobileStorageTracksAction.UnselectAllTracks -> unselectAllTracks()
            is MobileStorageTracksAction.ShowPlayListDialog -> showPlayListDialog()
            is MobileStorageTracksAction.ClosePlayListDialog -> hidePlayListDialog()
        }
    }

    private fun loadMobileStorageTracks() {
        state.tryEmit(state.value.copy(isLoading = true))
        state.tryEmit(
            state.value.copy(
                storageTracks = Pager(
                    PagingConfig(pageSize = INIT_PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)
                ) {
                    mobileStorageTracksPagingSource
                }.flow
                    .cachedIn(viewModelScope),
                isLoading = false
            )
        )
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