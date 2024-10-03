package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain

sealed class PlaylistErrors {

    data class Error(val message: String) : PlaylistErrors()
}