package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
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
}