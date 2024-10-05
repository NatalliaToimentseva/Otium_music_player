package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.folderTracks.domain

import com.example.otiummusicplayer.models.networkPart.TrackModel

sealed class FoldersTracksScreenAction {

    data class LoadFolderTracks(val folderId: Int) : FoldersTracksScreenAction()

    data object AddTrackToPlayList : FoldersTracksScreenAction()

    data class AddItemToSelected(val item: TrackModel) : FoldersTracksScreenAction()

    data class IsShowPermissionDialog(val isShow: Boolean) : FoldersTracksScreenAction()
}