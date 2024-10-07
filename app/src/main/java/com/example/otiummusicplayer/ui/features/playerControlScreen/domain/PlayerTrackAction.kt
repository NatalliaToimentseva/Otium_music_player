package com.example.otiummusicplayer.ui.features.playerControlScreen.domain

sealed class PlayerTrackAction {

    data class Init(val tracks: String, val itemId: String) : PlayerTrackAction()

    data class SetCurrentPosition(val position: Int) : PlayerTrackAction()

    data object Play : PlayerTrackAction()

    data object Stop : PlayerTrackAction()

    data object LoopTrack : PlayerTrackAction()

    data object PlayNext : PlayerTrackAction()

    data object PlayPrevious : PlayerTrackAction()

    data object DownloadTrack : PlayerTrackAction()

    data object ChooseIfFavorite : PlayerTrackAction()
}
