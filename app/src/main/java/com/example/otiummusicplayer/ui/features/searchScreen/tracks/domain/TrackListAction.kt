package com.example.otiummusicplayer.ui.features.searchScreen.tracks.domain

sealed class TrackListAction {

    data class Init(val id: String) : TrackListAction()

    data object ClearError : TrackListAction()
}