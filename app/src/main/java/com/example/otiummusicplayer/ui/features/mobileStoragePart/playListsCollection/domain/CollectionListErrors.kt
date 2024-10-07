package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.domain

sealed class PlaylistErrors {

    data class Error(val message: String) : PlaylistErrors()
}