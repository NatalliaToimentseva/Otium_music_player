package com.example.otiummusicplayer.ui.features.playerControlScreen.domain

sealed class PlayerTrackAction {

    data class Init(val tracks: String, val itemId: String) : PlayerTrackAction()

    data class SetCurrentPosition(val position: Float) : PlayerTrackAction()

    data object ApplyCurrentPosition : PlayerTrackAction()

    data object Play : PlayerTrackAction()

    data object SetShuffleMode : PlayerTrackAction()

    data object LoopTrack : PlayerTrackAction()

    data object PlayNext : PlayerTrackAction()

    data object PlayPrevious : PlayerTrackAction()

    data object DownloadTrack : PlayerTrackAction()

    data object ChooseIfFavorite : PlayerTrackAction()
}
