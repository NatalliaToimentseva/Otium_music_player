package com.example.otiummusicplayer.ui.features.playerControlScreen.domain

import android.graphics.Bitmap
import android.media.MediaPlayer
import com.example.otiummusicplayer.models.TrackModel

data class PlayerTrackState(
    val mediaPlayer: MediaPlayer? = null,
    val tracks: List<TrackModel>? = null,
    val currentTrack: TrackModel? = null,
    val imageBitmap: Bitmap? = null,
    val currentPosition: Int = 0,
    val isPlayed: Boolean = false,
    val isPlayerLooping: Boolean = false,
    val isLoading: Boolean = false
)