package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain

import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel

sealed class CollectionListAction {

    data class IsShowPermissionDialog(val isShow: Boolean) : CollectionListAction()

    data object GetAllPlaylists : CollectionListAction()

    data class SelectPlaylist(val item: PlayerPlayListModel) : CollectionListAction()

    data object UnselectAll : CollectionListAction()

    data object DeletePlaylist : CollectionListAction()

    data class ShowPlaylistDialog(val isShow: Boolean) : CollectionListAction()

    data class ShowRenamePlaylistDialog(val playlist: PlayerPlayListModel) : CollectionListAction()

    data class SetNewPlaylistTitleFromDialog(val title: String) : CollectionListAction()

    data class AddPlaylist(val title: String) : CollectionListAction()

    data class RenamePlaylist(val playlist: PlayerPlayListModel, val newTitle: String) :
        CollectionListAction()

    data object HideDialog : CollectionListAction()

    data object ClearError : CollectionListAction()
}