package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain

sealed class CollectionListAction {

    data class AddPlaylist(val title: String) : CollectionListAction()

    data class DeletePlaylist(val id: Long) : CollectionListAction()

    data class SetTitleFromDialog(val title: String) : CollectionListAction()

    data object GetAllPlaylists : CollectionListAction()

    data object ShowDialog : CollectionListAction()

    data object HideDialog : CollectionListAction()

    data object ClearError : CollectionListAction()
}