package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain

import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel

sealed class CollectionListAction {

    data class AddPlaylist(val title: String) : CollectionListAction()

    data class SetTitleFromDialog(val title: String) : CollectionListAction()

    data class SelectItem(val item: PlayerPlayListModel) : CollectionListAction()

    data object DeletePlaylist : CollectionListAction()

    data object GetAllPlaylists : CollectionListAction()

    data object ShowDialog : CollectionListAction()

    data object HideDialog : CollectionListAction()

    data object ClearError : CollectionListAction()
}