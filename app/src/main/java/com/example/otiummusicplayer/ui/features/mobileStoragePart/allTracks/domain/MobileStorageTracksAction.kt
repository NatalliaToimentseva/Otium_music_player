package com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks.domain

import com.example.otiummusicplayer.models.TrackModel

sealed class MobileStorageTracksAction {

    data class IsShowPermissionDialog(val isShow: Boolean) : MobileStorageTracksAction()

    data object LoadStorageTracks : MobileStorageTracksAction()

    data class AddTrackToSelected(val item: TrackModel) : MobileStorageTracksAction()

    data object UnselectAllTracks : MobileStorageTracksAction()

    data object ShowPlayListDialog : MobileStorageTracksAction()

    data object ClosePlayListDialog : MobileStorageTracksAction()
}