package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.main.domain

sealed class FoldersAction {

    data object Init: FoldersAction()
    data object ClearError: FoldersAction()
}