package com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks.domain

import com.example.otiummusicplayer.models.TrackModel

sealed class FoldersTracksScreenAction {

    data class IsShowPermissionDialog(val isShow: Boolean) : FoldersTracksScreenAction()

    data class LoadFolderTracks(val folderId: Int) : FoldersTracksScreenAction()

    data class AddTrackToSelected(val item: TrackModel) : FoldersTracksScreenAction()

    data object UnselectAllTracks : FoldersTracksScreenAction()

    data object ShowPlayListDialog : FoldersTracksScreenAction()

    data object ClosePlayListDialog : FoldersTracksScreenAction()
}