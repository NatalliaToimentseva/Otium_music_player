package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.otiummusicplayer.models.networkPart.TrackModel
import com.example.otiummusicplayer.repository.dataSource.MobileStorageTracksPagingSource
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks.domain.MobileStorageTracksAction
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks.domain.MobileStorageTracksState
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
            MobileStorageTracksAction.LoadStorageTracks -> loadMobileStorageTracks()
            is MobileStorageTracksAction.IsShowPermissionDialog -> showPermissionDialog(action.isShow)
            MobileStorageTracksAction.AddTrackToPlayList -> addTrackToPlayList()
            is MobileStorageTracksAction.AddItemToSelected -> addItemToSelected(action.item)
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