package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.domain

sealed class FoldersAction {

    data object Init: FoldersAction()
    data object ClearError: FoldersAction()
}