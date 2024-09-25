package com.example.otiummusicplayer.ui.features.search.tracks.domain

sealed class TrackListAction {

    data class Init(val id: String) : TrackListAction()

    data object ClearError : TrackListAction()
}