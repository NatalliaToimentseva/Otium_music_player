package com.example.otiummusicplayer.ui.features.playerControlScreen

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.models.networkPart.TrackModel
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.PlayerTrackState
import com.example.otiummusicplayer.ui.features.playerControlScreen.playerElements.DownloadTrackUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val gson: Gson,
    private val favoriteUseCase: CheckInFavoriteUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deleteFromFavoriteUseCase: DeleteFromFavoriteUseCase,
    private val downloadTrackUseCase: DownloadTrackUseCase
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
            is PlayerTrackAction.SetCurrentPosition -> setPosition(action.position)
            is PlayerTrackAction.Init -> init(action.tracks, action.itemId)
            PlayerTrackAction.Play -> startPlayer()
            PlayerTrackAction.Stop -> pausePlayer()
            PlayerTrackAction.LoopTrack -> loopTrack()
            PlayerTrackAction.PlayNext -> playNext()
            PlayerTrackAction.PlayPrevious -> playPrevious()
            PlayerTrackAction.ChooseIfFavorite -> setIfFavorite()
            is PlayerTrackAction.DownloadTrack -> downloadTrack()
        }
    }

    private fun init(tracks: String, itemId: String) {
        if (state.value.tracks == null) {
            val listType = object : TypeToken<List<TrackModel>>() {}.type
            val trackList = gson.fromJson<List<TrackModel>>(tracks, listType)
            viewModelScope.launch(Dispatchers.IO) {
                val isFavorite = favoriteUseCase.checkIsTrackInFavorite(itemId)
                if (isFavorite != null) {
                    state.tryEmit(
                        state.value.copy(
                            tracks = trackList,
                            currentTrack = isFavorite
                        )
                    )
                } else {
                    state.tryEmit(
                        state.value.copy(
                            tracks = trackList,
                            currentTrack = trackList.firstOrNull { it.id == itemId })
                    )
                }
                preparePlayer()
                startPlayer()
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
            state.value.mediaPlayer?.setOnCompletionListener {
                playNext()
            }
            state.tryEmit(state.value.copy(isPlayed = true))
        } catch (e: IOException) {
            e.stackTrace
        }
    }

    private fun pausePlayer() {
        try {
            if (state.value.mediaPlayer?.isPlaying == true) {
                state.value.mediaPlayer?.pause()
                state.tryEmit(state.value.copy(isPlayed = false))
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

    private fun setIfFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value.currentTrack?.let { track ->
                if (track.isFavorite) {
                    state.tryEmit(state.value.copy(currentTrack = track.copy(isFavorite = false)))
                    deleteFromFavoriteUseCase.deleteTrackFromFavorite(track)
                } else {
                    state.tryEmit(state.value.copy(currentTrack = track.copy(isFavorite = true)))
                    addToFavoriteUseCase.addTrackToFavorite(track)
                }
            }
        }
    }

    private fun downloadTrack() {
        state.value.currentTrack?.let { track ->
            downloadTrackUseCase.downloadTrack(track.audioDownload)
        }
    }

    private fun synchroniseLoop() {
        state.value.mediaPlayer?.isLooping = state.value.isPlayerLooping
    }

    private fun setPosition(position: Int) {
        state.tryEmit(state.value.copy(currentPosition = position))
    }
}
