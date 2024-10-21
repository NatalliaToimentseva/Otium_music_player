package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.otiummusicplayer.models.TrackModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@UnstableApi
class MediaPlayerServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    token: SessionToken,
    private val controllerFuture: ListenableFuture<MediaController>,
    private val mediaSource: MediaSource
) {

    private val _currentPlayingAudio: MutableStateFlow<TrackModel?> = MutableStateFlow(null)
    val currentPlayingAudio: StateFlow<TrackModel?> get() = _currentPlayingAudio
    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying
    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> get() = _isConnected
    private val browserFuture = MediaBrowser.Builder(context, token).buildAsync()
    private val listener = MediaControllerListener()
    private var mediaBrowser: MediaBrowser? = null
    private var mediaController: MediaController? = null
    private var audioList = MutableStateFlow<List<TrackModel>>(arrayListOf())
    private var currentAudioId = MutableStateFlow<Int>(0)

    init {
        browserFuture.addListener({
            mediaBrowser = browserFuture.get()
            connectToServer()
            _isConnected.value = true
        }, MoreExecutors.directExecutor())
    }

    suspend fun loadData() {
        mediaSource.refresh()
        mediaSource.load()
    }

    fun disconnect() {
        mediaBrowser?.removeListener(listener)
        mediaBrowser = null
    }

    fun playAudio(tracks: List<TrackModel>, audioId: Int) {
        audioList.value = tracks
        currentAudioId.value = audioId
        mediaSource.whenReady {
            val itemToPlay =
                mediaSource.audioMediaItems.find { it.mediaId == currentAudioId.value.toString() }
            val index = mediaSource.audioMediaItems.indexOf(itemToPlay)
            mediaController?.run {
                clearMediaItems()
                addMediaItems(mediaSource.audioMediaItems)
                prepare()
                seekTo(index, 0)
                playWhenReady = true
            }
        }
    }

    fun detectCurrentPosition(): Float = mediaController?.currentPosition?.toFloat() ?: 0F

    fun playTrack() {
        mediaController?.play()
    }

    fun pauseTrack() {
        mediaController?.pause()
    }

    fun seekTo(position: Long) {
        mediaController?.seekTo(
            position
        )
    }

    fun skipToNext() {
        mediaController?.seekToNextMediaItem()
    }

    fun skipToPrevious() {
        mediaController?.seekToPreviousMediaItem()
    }

    fun onShuffleMode() {
        mediaController?.shuffleModeEnabled = true
    }

    fun offShuffleMode() {
        mediaController?.shuffleModeEnabled = false
    }

    fun onRepeatMode() {
        mediaController?.repeatMode = Player.REPEAT_MODE_ONE
    }

    fun offRepeatMode() {
        mediaController?.repeatMode = Player.REPEAT_MODE_OFF
    }

    private fun connectToServer() {
        mediaBrowser?.run {
            if (isConnected) {
                controllerFuture.addListener({
                    mediaController = controllerFuture.get()
                }, MoreExecutors.directExecutor())
                mediaBrowser?.addListener(listener)
            }
        }
    }

    inner class MediaControllerListener : Player.Listener {

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)

            _currentPlayingAudio.value = mediaItem?.let { item ->
                audioList.value.find {
                    it.id == item.mediaId
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            _isPlaying.value = isPlaying
        }
    }
}