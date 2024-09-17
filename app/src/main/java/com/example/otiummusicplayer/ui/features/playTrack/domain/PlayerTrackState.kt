package com.example.otiummusicplayer.ui.features.playTrack.domain

import android.media.MediaPlayer
import com.example.otiummusicplayer.ui.features.playTrack.URL

data class PlayerTrackState(
    val mediaPlayer: MediaPlayer? = null,
    val duration: Int? = 0,
    val url: String = URL,
    val currentPosition: Int = 0,
    val isPlayed: Boolean = false,
    val isPlayerLooping: Boolean = false
)