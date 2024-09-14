package com.example.otiummusicplayer.ui.features.playTrack

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

//TEST
const val URL =
    "https://prod-1.storage.jamendo.com/?trackid=1467858&format=mp31&from=QeBfVL5FlSP5h7JRU1OYQQ%3D%3D%7C5OZLVhPfv%2Ff9Z7jFE1nvDw%3D%3D"

class PlayTrackViewModel : ViewModel() {

    val mediaPlayer: MediaPlayer = MediaPlayer()
    val duration = MutableStateFlow<Int?>(null)
    private val url = MutableStateFlow(URL)
    private val isPlayerLooping = false

    fun processAction(action: PlayerTrackAction) {
        when (action) {
            PlayerTrackAction.Play -> startPlayer()
            PlayerTrackAction.Stop -> pausePlayer()
            is PlayerTrackAction.SetURL -> setUrl(URL)
        }
    }

    private fun setUrl(trackUrl: String) {
        viewModelScope.launch {
            url.tryEmit(trackUrl)
            preparePlayer()
        }
    }

    private fun preparePlayer() {
        try {
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            mediaPlayer.setDataSource(url.value)
            mediaPlayer.prepare()
            viewModelScope.launch {
                duration.tryEmit(mediaPlayer.duration)
            }
        } catch (e: IOException) {
            e.stackTrace
        }
    }

    private fun startPlayer() {
        try {
            mediaPlayer.start()
        } catch (e: IOException) {
            e.stackTrace
        }
    }

    private fun pausePlayer() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        } catch (e: Exception) {
            Log.d("AAA", "Error: ${e.message} + ${e.stackTrace}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.reset()
        mediaPlayer.release()
    }
}
