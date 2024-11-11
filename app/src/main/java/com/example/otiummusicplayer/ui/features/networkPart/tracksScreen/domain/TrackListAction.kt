package com.example.otiummusicplayer.ui.features.networkPart.tracksScreen.domain

sealed class TrackListAction {

    data class Init(val id: String) : TrackListAction()

    data object ClearError : TrackListAction()
}