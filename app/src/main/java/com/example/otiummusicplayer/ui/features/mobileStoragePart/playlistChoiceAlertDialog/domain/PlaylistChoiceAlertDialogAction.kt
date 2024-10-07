package com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog.domain

import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel

sealed class PlaylistChoiceAlertDialogAction {

    data object LoadPlaylists : PlaylistChoiceAlertDialogAction()

    data class SetSelectedTracks(val selectedTracks: List<TrackModel>) :
        PlaylistChoiceAlertDialogAction()

    data class SelectPlayListFromDialog(val list: PlayerPlayListModel) :
        PlaylistChoiceAlertDialogAction()

    data object AddTracksToPlayList : PlaylistChoiceAlertDialogAction()
}