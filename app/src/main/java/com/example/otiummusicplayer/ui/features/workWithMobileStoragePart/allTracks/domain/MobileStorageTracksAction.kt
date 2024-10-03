package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks.domain

sealed class MobileStorageTracksAction {

    data object LoadStorageTracks : MobileStorageTracksAction()

    data class IsShowPermissionDialog(val isShow: Boolean) : MobileStorageTracksAction()
}