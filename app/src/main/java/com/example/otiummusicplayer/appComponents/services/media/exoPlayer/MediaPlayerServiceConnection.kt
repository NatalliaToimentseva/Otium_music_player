package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import android.content.Context
import android.util.Log
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
    private val _currentTrackPosition: MutableStateFlow<Long> = MutableStateFlow(0)
    val currentTrackPosition: StateFlow<Long> get() = _currentTrackPosition
    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying
    private val _isReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> get() = _isReady
    private val browserFuture = MediaBrowser.Builder(context, token).buildAsync()
    private var mediaBrowser: MediaBrowser? = null
    private var mediaController: MediaController? = null
    private var audioList = MutableStateFlow<List<TrackModel>>(arrayListOf())

    init {
        browserFuture.addListener({
            mediaBrowser = browserFuture.get()
            connectToServer()
        }, MoreExecutors.directExecutor())
    }

    suspend fun loadData() {
        mediaSource.refresh()
        mediaSource.load()
    }

    private fun connectToServer() {
        mediaBrowser?.run {
            if (isConnected) {
                controllerFuture.addListener({
                    mediaController = controllerFuture.get()
                }, MoreExecutors.directExecutor())
                mediaController?.addListener(MediaControllerListener())
                mediaSource.whenReady {
                    mediaController?.run {
                        clearMediaItems()
                        addMediaItems(mediaSource.audioMediaItems)
                        prepare()
                    }
                    _isReady.value = true
                }
            }
        }
    }

    fun disconnect() {
        mediaBrowser = null
    }

    fun playAudio(tracks:List<TrackModel>) {
        audioList.value = tracks
    }

    fun playFromMedia(mediaId: String) {
        mediaController?.run {
            seekToDefaultPosition(mediaId.toInt())
            playWhenReady
        }
    }

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

    inner class MediaControllerListener : Player.Listener {

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            Log.d("AAA", "ServiceConnection onMediaItemTransition = $mediaItem")
            _currentPlayingAudio.value = mediaItem?.let { item ->
                audioList.value.find {
                    it.id == item.mediaId
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            Log.d("AAA", "ServiceConnection onIsPlayingChanged(isPlaying = $isPlaying")
            _isPlaying.value = isPlaying
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            Log.d("AAA", "ServiceConnection onPositionDiscontinuity newPosition = ${newPosition.positionMs} reason $reason")
            _currentTrackPosition.value = newPosition.positionMs
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            Log.d("AAA", "ServiceConnection onPlaybackStateChanged = $playbackState")
        }

        override fun onSeekBackIncrementChanged(seekBackIncrementMs: Long) {
            super.onSeekBackIncrementChanged(seekBackIncrementMs)
            Log.d("AAA", "ServiceConnection onSeekBackIncrementChanged = $seekBackIncrementMs")
        }

        override fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {
            super.onSeekForwardIncrementChanged(seekForwardIncrementMs)
            Log.d("AAA", "ServiceConnection onSeekForwardIncrementChanged = $seekForwardIncrementMs")
        }
    }
}