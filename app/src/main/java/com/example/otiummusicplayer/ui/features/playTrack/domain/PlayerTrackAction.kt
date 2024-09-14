package com.example.otiummusicplayer.ui.features.playTrack.domain

sealed class PlayerTrackAction {

    data object Play : PlayerTrackAction()
    data object Stop : PlayerTrackAction()
    data class SetURL(val url: String) : PlayerTrackAction()
}
