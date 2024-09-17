package com.example.otiummusicplayer.ui.features.playTrack.domain

sealed class PlayerTrackAction {

    data class SetUrl(val url: String) : PlayerTrackAction()
    data class SetPlayed(val isPlayed: Boolean) : PlayerTrackAction()
    data class SetCurrentPosition(val position: Int) : PlayerTrackAction()
    data object Play : PlayerTrackAction()
    data object Stop : PlayerTrackAction()
}
