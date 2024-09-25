package com.example.otiummusicplayer.ui.features.playTrack.domain

import com.example.otiummusicplayer.models.TrackModel

sealed class PlayerTrackAction {

    data class Init(val tracks: String, val itemId: String) : PlayerTrackAction()

    data class SetPlayed(val isPlayed: Boolean) : PlayerTrackAction()

    data class SetCurrentPosition(val position: Int) : PlayerTrackAction()

    data object ClearError : PlayerTrackAction()

    data object Play : PlayerTrackAction()

    data object Stop : PlayerTrackAction()

    data object LoopTrack: PlayerTrackAction()

    data object PlayNext: PlayerTrackAction()

    data object PlayPrevious: PlayerTrackAction()
}
