package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import android.util.Log
import androidx.annotation.OptIn
import com.example.otiummusicplayer.appComponents.services.media.domain.MusicDataController
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.formatTimeToMls
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource2
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

typealias OnReadyListener = (Boolean) -> Unit

class MediaSource @Inject constructor(
    private val musicDataController: MusicDataController
) {
    val currentAudio = MutableStateFlow<MediaItem?>(null)
    var audioMediaItems : List<MediaItem> = arrayListOf()
    private val onReadyListeners: MutableList<OnReadyListener> = mutableListOf()
    private var state: AudioSourceState = AudioSourceState.STATE_CREATED
        set(value) {
            if (
                value == AudioSourceState.STATE_INITIALIZED ||
                value == AudioSourceState.STATE_ERROR
            ) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener: OnReadyListener ->
                        listener.invoke(isReady)
                    }
                }
            } else {
                field = value
            }
        }
    private val isReady: Boolean
        get() = state == AudioSourceState.STATE_INITIALIZED

//    fun whenReady() = audioMediaItems

    fun whenReady(listener: OnReadyListener): Boolean {
        Log.d("AAA", "MediaSource whenReady was called")
        return if (
            state == AudioSourceState.STATE_CREATED ||
            state == AudioSourceState.STATE_INITIALIZING
        ) {
            Log.d("AAA", "MediaSource whenReady if")
            onReadyListeners += listener
            false
        } else {
            Log.d("AAA", "MediaSource whenReady else")
            listener.invoke(isReady)
            true
        }
    }

    suspend fun load() {
        Log.d("AAA", "MediaSource load()")
        state = AudioSourceState.STATE_INITIALIZING
        musicDataController.listenCurrentMusicList().collect { list ->
            transformData(list)
        }
        musicDataController.listenCurrentPosition().collect { audio ->
            buildMediaItem(audio)
        }
    }

//    @OptIn(UnstableApi::class)
//    fun asMediaSource(dataSource: CacheDataSource.Factory): ConcatenatingMediaSource2 { //return items suitable for exoplayer to play one by one
//        val concatenatingMediaSource = ConcatenatingMediaSource2.Builder()
//        audioMediaItems.forEach { mediaItem ->
//            val mediaSource = ProgressiveMediaSource
//                .Factory(dataSource)
//                .createMediaSource(mediaItem)
//            concatenatingMediaSource.add(mediaSource, 120L)
//        }
//        Log.d("AAA", "MediaSource asMediaSource audioMediaMetaData= $audioMediaItems")
//        Log.d("AAA", "MediaSource asMediaSource concatenatingMediaSource= $concatenatingMediaSource")
//        return concatenatingMediaSource.build()
//    }

    //    @OptIn(UnstableApi::class)
//    fun asMediaItem() =
//        audioMediaMetaData.map { metaData -> //this function gives metadata to display by UI
//            val description = MediaDescriptionCompat.Builder()
//                .setMediaId(metaData.description.mediaId)
//                .setTitle(metaData.description.title)
//                .setMediaUri(metaData.description.mediaUri)
//                .build()
//            MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
//        }.toMutableList()
//
    fun refresh() {
        onReadyListeners.clear()
        state = AudioSourceState.STATE_CREATED
    }

    @OptIn(UnstableApi::class)
    private fun transformData(data: List<TrackModel>) {
        Log.d("AAA", "MediaSource transformData data = $data")
        audioMediaItems = data.map { audio ->
            buildMediaItem(audio)
        }
        Log.d("AAA", "MediaSource transformData audioMediaItems = $audioMediaItems")
//        data.map { audio ->
//            MediaMetadata.Builder()
//                .setTitle(audio.name)
//                .setArtist(audio.artistName)
//                .setAlbumTitle(audio.albumName)
//                .setArtworkData()
//                .setDurationMs(formatTimeToMls(audio.duration).toLong())

//                .setString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id)
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.name)
//                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artistName)
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, audio.albumName)
//                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.audio)
//                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.image)
//                .putLong(
//                    MediaMetadataCompat.METADATA_KEY_DURATION,
//                    formatTimeToMls(audio.duration).toLong()
//                )
//                .build()
//        }
        state = AudioSourceState.STATE_INITIALIZED
    }

    private fun createCurrentItem(audio: TrackModel) {
        currentAudio.value = buildMediaItem(audio)
    }

    @OptIn(UnstableApi::class)
    private fun buildMediaItem(audio: TrackModel): MediaItem {
        return MediaItem.Builder()
            .setMediaId(audio.id)
            .setUri(audio.audio)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(audio.name)
                    .setArtist(audio.artistName)
                    .setAlbumTitle(audio.albumName)
//                        .setArtworkData()
                    .setDurationMs(formatTimeToMls(audio.duration).toLong())
                    .build()
            )
            .build()
    }
}


enum class AudioSourceState {

    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR,
}