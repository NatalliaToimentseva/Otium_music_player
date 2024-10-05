package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.main.domain.FoldersAction
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.main.domain.FoldersResult
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.main.domain.FoldersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersScreenViewModel @Inject constructor(
    private val loadFoldersUseCase: LoadFoldersUseCase
) : ViewModel() {

    val state = MutableStateFlow(FoldersState())

    init {
        processAction(FoldersAction.Init)
    }

    fun processAction(action: FoldersAction) {
        when (action) {
            FoldersAction.Init -> loadFolders()
            FoldersAction.ClearError -> clearError()
        }
    }

    private fun loadFolders() {
        viewModelScope.launch(Dispatchers.IO) {
            loadFoldersUseCase.loadFolders().collect { result ->
                handleResult(result)
            }
        }
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }

    private fun handleResult(result: FoldersResult) {
        when (result) {
            is FoldersResult.Error -> state.tryEmit(state.value.copy(error = result.message))
            FoldersResult.Loading -> state.tryEmit(state.value.copy(isLoading = true))
            is FoldersResult.Success -> state.tryEmit(
                state.value.copy(
                    folders = result.data,
                    isLoading = false
                )
            )
        }
    }
}