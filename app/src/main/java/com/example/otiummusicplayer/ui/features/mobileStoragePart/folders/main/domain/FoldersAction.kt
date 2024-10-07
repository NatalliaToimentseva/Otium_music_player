package com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.main.domain

sealed class FoldersAction {

    data object Init: FoldersAction()
    data object ClearError: FoldersAction()
}