package com.example.otiummusicplayer.ui.features.playTrack.domain

sealed class PlayerTrackAction {

    data class Init(val id: Int, val itemId: String) : PlayerTrackAction()
    data class SetPlayed(val isPlayed: Boolean) : PlayerTrackAction()
    data class SetCurrentPosition(val position: Int) : PlayerTrackAction()
    data object ClearError : PlayerTrackAction()
    data object Play : PlayerTrackAction()
    data object Stop : PlayerTrackAction()
    data object LoopTrack: PlayerTrackAction()
    data object PlayNext: PlayerTrackAction()
    data object PlayPrevious: PlayerTrackAction()
}
