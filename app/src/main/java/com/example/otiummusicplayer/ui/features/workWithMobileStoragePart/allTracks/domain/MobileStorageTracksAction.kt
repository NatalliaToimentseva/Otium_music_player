package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks.domain

import com.example.otiummusicplayer.models.networkPart.TrackModel

sealed class MobileStorageTracksAction {

    data object LoadStorageTracks : MobileStorageTracksAction()

    data object AddTrackToPlayList : MobileStorageTracksAction()

    data class AddItemToSelected(val item: TrackModel) : MobileStorageTracksAction()

    data class IsShowPermissionDialog(val isShow: Boolean) : MobileStorageTracksAction()
}