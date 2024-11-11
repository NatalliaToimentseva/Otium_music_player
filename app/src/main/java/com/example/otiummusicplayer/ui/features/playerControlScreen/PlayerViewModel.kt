package com.example.otiummusicplayer.ui.features.playerControlScreen

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.otiummusicplayer.controllers.MediaPlayerController
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.AddToFavoriteUseCase
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.CheckInFavoriteUseCase
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.DeleteFromFavoriteUseCase
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.DownloadTrackUseCase
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.PlayerTrackState
import com.example.otiummusicplayer.utils.loadPicture
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PLAYBACK_UPDATE_INTERVAL = 1000L

@HiltViewModel
class PlayerViewModel @OptIn(UnstableApi::class)
@Inject constructor(
    private val favoriteUseCase: CheckInFavoriteUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deleteFromFavoriteUseCase: DeleteFromFavoriteUseCase,
    private val downloadTrackUseCase: DownloadTrackUseCase,
    private val serviceConnection: MediaPlayerController
) : ViewModel() {

    val state = MutableStateFlow(PlayerTrackState())

    init {
        viewModelScope.launch(Dispatchers.Main) {
            serviceConnection.isConnected.collect { isConnected ->
                if (isConnected) {
                    state.value.tracks?.let { tracks ->
                        state.value.currentTrack?.let { track ->
                            serviceConnection.playAudio(tracks, track.id)
                        }
                    }
                }
            }
        }
        viewModelScope.launch {
            serviceConnection.currentPlayingAudio.collect { media ->
                if (media != null) {
                    val newCurrentTrack = state.value.tracks?.find { track ->
                        track.id == media.mediaId
                    }
                    state.tryEmit(state.value.copy(currentTrack = newCurrentTrack))
                    setBitmapImage()
                }
            }
        }
        viewModelScope.launch {
            serviceConnection.isPlaying.collect { isPlaying ->
                state.tryEmit(
                    state.value.copy(
                        isPlayed = isPlaying,
                    )
                )
                if (isPlaying) {
                    updatePlayBack()
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCleared() {
        super.onCleared()
        serviceConnection.disconnect()
    }

    fun processAction(action: PlayerTrackAction) {
        when (action) {
            is PlayerTrackAction.Init -> init(action.tracks, action.itemId)
            is PlayerTrackAction.SetCurrentPosition -> setPosition(action.position)
            is PlayerTrackAction.Play -> playAudio()
            is PlayerTrackAction.SetShuffleMode -> setShuffleMod()
            is PlayerTrackAction.LoopTrack -> loopTrack()
            is PlayerTrackAction.PlayNext -> skipToNext()
            is PlayerTrackAction.PlayPrevious -> skipToPrevious()
            is PlayerTrackAction.ChooseIfFavorite -> setIfFavorite()
            is PlayerTrackAction.DownloadTrack -> downloadTrack()
        }
    }

    @OptIn(UnstableApi::class)
    private fun init(tracks: String, itemId: String) {
        if (state.value.tracks == null) {
            val listType = object : TypeToken<List<TrackModel>>() {}.type
            val trackList = GsonBuilder().create().fromJson<List<TrackModel>>(tracks, listType)
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
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun playAudio() {
        if (state.value.isPlayed) {
            serviceConnection.pauseTrack()
        } else {
            serviceConnection.playTrack()
        }
    }

    @OptIn(UnstableApi::class)
    private fun skipToNext() {
        serviceConnection.skipToNext()
    }

    @OptIn(UnstableApi::class)
    private fun skipToPrevious() {
        serviceConnection.skipToPrevious()
    }

    private fun setPosition(position: Float) {
        state.tryEmit(state.value.copy(currentPosition = position))
        seekTo()
    }

    @OptIn(UnstableApi::class)
    private fun seekTo() {
        serviceConnection.seekTo(
            state.value.currentPosition.toLong()
        )
    }

    @OptIn(UnstableApi::class)
    private fun updatePlayBack() {
        viewModelScope.launch {
            while (state.value.isPlayed) {
                state.tryEmit(
                    state.value.copy(
                        currentPosition = serviceConnection.detectCurrentPosition()
                    )
                )
                delay(PLAYBACK_UPDATE_INTERVAL)
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun setShuffleMod() {
        if (state.value.isShuffle) {
            serviceConnection.offShuffleMode()
            state.tryEmit(state.value.copy(isShuffle = false))
        } else {
            serviceConnection.onShuffleMode()
            state.tryEmit(state.value.copy(isShuffle = true))
        }
    }

    @OptIn(UnstableApi::class)
    private fun loopTrack() {
        if (state.value.isPlayerLooping) {
            serviceConnection.offRepeatMode()
            state.tryEmit(state.value.copy(isPlayerLooping = false))
        } else {
            serviceConnection.onRepeatMode()
            state.tryEmit(state.value.copy(isPlayerLooping = true))
        }
    }

    private fun setIfFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value.currentTrack?.let { track ->
                if (track.isFavorite == true) {
                    val notFavorite = track.copy(isFavorite = false)
                    state.tryEmit(state.value.copy(currentTrack = notFavorite))
                    deleteFromFavoriteUseCase.deleteTrackFromFavorite(notFavorite)
                } else {
                    val favorite = track.copy(isFavorite = true)
                    state.tryEmit(state.value.copy(currentTrack = favorite))
                    addToFavoriteUseCase.addTrackToFavorite(favorite)
                }
            }
        }
    }

    private fun downloadTrack() {
        state.value.currentTrack?.let { track ->
            track.audioDownload?.let { downloadTrackUseCase.downloadTrack(it) }
        }
    }

    private fun setBitmapImage() {
        state.value.currentTrack?.let { track ->
            state.tryEmit(state.value.copy(imageBitmap = loadPicture(track.path)))
        }
    }
}