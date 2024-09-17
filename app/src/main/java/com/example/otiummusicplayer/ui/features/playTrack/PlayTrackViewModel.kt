package com.example.otiummusicplayer.ui.features.playTrack

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class PlayTrackViewModel : ViewModel() {

    val state = MutableStateFlow(PlayerTrackState())

    init {
        state.tryEmit(state.value.copy(mediaPlayer = MediaPlayer()))
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        state.value.mediaPlayer?.reset()
        state.value.mediaPlayer?.release()
    }

    fun processAction(action: PlayerTrackAction) {
        when (action) {
            is PlayerTrackAction.SetUrl -> setUrl(URL) //take from action!!
            is PlayerTrackAction.SetPlayed -> setPlayed(action.isPlayed)
            is PlayerTrackAction.SetCurrentPosition -> setPosition(action.position)
            PlayerTrackAction.Play -> startPlayer()
            PlayerTrackAction.Stop -> pausePlayer()
        }
    }

    private fun setUrl(trackUrl: String) {
        viewModelScope.launch {
            state.tryEmit(state.value.copy(url = trackUrl))
        }
    }

    private fun preparePlayer() {
        try {
            state.value.mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            state.value.mediaPlayer?.setDataSource(state.value.url)
            state.value.mediaPlayer?.prepare()
            viewModelScope.launch {
                state.tryEmit(state.value.copy(duration = state.value.mediaPlayer?.duration))
            }
        } catch (e: IOException) {
            e.stackTrace
        }
    }

    private fun startPlayer() {
        try {
            state.value.mediaPlayer?.start()
        } catch (e: IOException) {
            e.stackTrace
        }
    }

    private fun pausePlayer() {
        try {
            if (state.value.mediaPlayer?.isPlaying == true) {
                state.value.mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun setPlayed(isPlayed: Boolean) {
        state.tryEmit(state.value.copy(isPlayed = isPlayed))
    }

    private fun setPosition(position: Int) {
        state.tryEmit(state.value.copy(currentPosition = position))
    }
}
