package com.example.otiummusicplayer.ui.features.playerControlScreen

import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.otiummusicplayer.appComponents.services.media.constants.K.PLAYBACK_UPDATE_INTERVAL
import com.example.otiummusicplayer.appComponents.services.media.domain.MusicDataController
import com.example.otiummusicplayer.appComponents.services.media.exoPlayer.MediaPlayerServiceConnection
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.PlayerTrackState
import com.example.otiummusicplayer.ui.features.playerControlScreen.playerElements.DownloadTrackUseCase
import com.example.otiummusicplayer.utils.loadPicture
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @OptIn(UnstableApi::class)
@Inject constructor(
    private val gson: Gson,
    private val favoriteUseCase: CheckInFavoriteUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deleteFromFavoriteUseCase: DeleteFromFavoriteUseCase,
    private val downloadTrackUseCase: DownloadTrackUseCase,
    private val musicDataController: MusicDataController,
    private val serviceConnection: MediaPlayerServiceConnection
) : ViewModel() {

    val state = MutableStateFlow(PlayerTrackState())

    init {
        viewModelScope.launch (Dispatchers.Main){
            serviceConnection.isReady.collect { isReady ->
                playAudio()
            }
        }
        viewModelScope.launch {
            serviceConnection.currentPlayingAudio.collect { value ->
                if (value != null) {
                    val newCurrentTrack = state.value.tracks?.find { track ->
                        track.id == value.id

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
            is PlayerTrackAction.ApplyCurrentPosition -> seekTo()
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
                musicDataController.currentMusicData.tryEmit(trackList)
                serviceConnection.loadData()
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun playAudio() {
        state.value.currentTrack?.let { currentAudio ->
            state.value.tracks?.let { list ->
                serviceConnection.playAudio(list)
                if (currentAudio.id == serviceConnection.currentPlayingAudio.value?.id) {
                    if (state.value.isPlayed) {
                        serviceConnection.pauseTrack()
                    } else {
                        serviceConnection.playTrack()
                    }
                } else {
                    serviceConnection.playFromMedia(currentAudio.id)
                }
            }
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
    }

    @OptIn(UnstableApi::class)
    private fun seekTo() {
        serviceConnection.seekTo(
            state.value.currentPosition.toLong()
        )
    }

    private fun updatePlayBack() {
        viewModelScope.launch {
            while (state.value.isPlayed) {
//                Log.d("AAA", " VW currentTrackPosition from serviceConnection= ${serviceConnection.currentTrackPosition.value.toFloat()}")
                state.tryEmit(
                    state.value.copy(
                        currentPosition = serviceConnection.currentTrackPosition.value.toFloat()
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