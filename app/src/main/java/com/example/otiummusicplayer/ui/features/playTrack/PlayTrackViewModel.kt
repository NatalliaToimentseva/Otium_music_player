package com.example.otiummusicplayer.ui.features.playTrack

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackState
import com.example.otiummusicplayer.ui.features.search.screens.tracks.LoadTracksByAlbumIdUseCase
import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PlayTrackViewModel @Inject constructor(
    private val loadTracksByAlbumIdUseCase: LoadTracksByAlbumIdUseCase
) : ViewModel() {

    val state = MutableStateFlow(PlayerTrackState())

    init {
        try {
            state.tryEmit(state.value.copy(mediaPlayer = MediaPlayer()))
            state.value.mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        } catch (e: IOException) {
            e.stackTrace
        }
    }

    override fun onCleared() {
        super.onCleared()
        state.value.mediaPlayer?.reset()
        state.value.mediaPlayer?.release()
    }

    fun processAction(action: PlayerTrackAction) {
        when (action) {
            is PlayerTrackAction.SetPlayed -> setPlayed(action.isPlayed)
            is PlayerTrackAction.SetCurrentPosition -> setPosition(action.position)
            is PlayerTrackAction.Init -> init(action.id, action.itemId)
            PlayerTrackAction.ClearError -> clearError()
            PlayerTrackAction.Play -> startPlayer()
            PlayerTrackAction.Stop -> pausePlayer()
            PlayerTrackAction.LoopTrack -> loopTrack()
            PlayerTrackAction.PlayNext -> playNext()
            PlayerTrackAction.PlayPrevious -> playPrevious()
        }
    }

    private fun init(id: Int, itemId: String) {
        if (state.value.tracks == null) {
            viewModelScope.launch(Dispatchers.IO) {
                loadTracksByAlbumIdUseCase.loadTracks(id).collect { result ->
                    handleResult(result)
                }
                state.value.tracks?.let { tracks ->
                    state.tryEmit(state.value.copy(currentTrack = tracks.firstOrNull { it.id == itemId }))
                }
                preparePlayer()
            }
        }
    }

    private fun preparePlayer() {
        try {
            state.value.currentTrack?.let { track ->
                state.value.mediaPlayer?.setDataSource(track.audio)
                state.value.mediaPlayer?.prepare()
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

    private fun playNext() {
        state.value.currentTrack?.let { track ->
            state.value.tracks?.let { tracks ->
                val index = state.value.tracks?.indexOfFirst { it.id == track.id }
                if (index != null && index != -1 && (index + 1) <= (tracks.size - 1)) {
                    state.value.mediaPlayer?.reset()
                    state.tryEmit(state.value.copy(currentTrack = tracks[index + 1]))
                    preparePlayer()
                    synchroniseLoop()
                    if (state.value.isPlayed) startPlayer()
                }
            }
        }
    }

    private fun playPrevious() {
        state.value.currentTrack?.let { track ->
            state.value.tracks?.let { tracks ->
                val index = state.value.tracks?.indexOfFirst { it.id == track.id }
                if (index != null && index != -1 && (index - 1) >= 0) {
                    state.value.mediaPlayer?.reset()
                    state.tryEmit(state.value.copy(currentTrack = tracks[index - 1]))
                    preparePlayer()
                    synchroniseLoop()
                    if (state.value.isPlayed) startPlayer()
                }
            }
        }
    }

    private fun loopTrack() {
        state.value.mediaPlayer?.let { player ->
            if (state.value.isPlayerLooping) {
                player.isLooping = false
                state.tryEmit(state.value.copy(isPlayerLooping = false))
            } else {
                player.isLooping = true
                state.tryEmit(state.value.copy(isPlayerLooping = true))
            }
        }
    }

    private fun synchroniseLoop() {
        state.value.mediaPlayer?.isLooping = state.value.isPlayerLooping
    }

    private fun setPlayed(isPlayed: Boolean) {
        state.tryEmit(state.value.copy(isPlayed = isPlayed))
    }

    private fun setPosition(position: Int) {
        state.tryEmit(state.value.copy(currentPosition = position))
    }

    private fun handleResult(result: TrackListResult) {
        when (result) {
            is TrackListResult.Error -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    error = "Error of loading: ${result.throwable.message}"
                )
            )

            TrackListResult.Loading -> state.tryEmit(state.value.copy(isLoading = true))
            is TrackListResult.Success -> state.tryEmit(
                state.value.copy(
                    isLoading = false,
                    tracks = result.data
                )
            )
        }
    }

    private fun clearError() {
        state.tryEmit(state.value.copy(error = null))
    }
}
