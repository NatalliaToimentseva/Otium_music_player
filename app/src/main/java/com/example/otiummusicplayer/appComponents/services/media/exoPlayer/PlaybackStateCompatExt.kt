package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

//import android.os.SystemClock
//import androidx.media3.session.legacy.PlaybackStateCompat
//
//inline val PlaybackStateCompat.isPlaying: Boolean
//    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
//            state == PlaybackStateCompat.STATE_PLAYING
//
//inline val PlaybackStateCompat.currentPosition: Long
//    get() = if(state == PlaybackStateCompat.STATE_PLAYING) {
//        val timeDelta = SystemClock.elapsedRealtime() - lastPositionUpdateTime
//        (position + (timeDelta * playbackSpeed)).toLong()
//    } else {
//        position
//    }