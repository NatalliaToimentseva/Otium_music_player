package com.example.otiummusicplayer.ui.features.workWithNetworkPart.tracksScreen.domain

sealed class TrackListAction {

    data class Init(val id: String) : TrackListAction()

    data object ClearError : TrackListAction()
}