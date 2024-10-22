package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.OptIn
import com.example.otiummusicplayer.appComponents.services.media.domain.MusicDataController
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.formatTimeToMls
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.utils.loadImageWithGlide
import com.example.otiummusicplayer.utils.loadPicture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

typealias OnReadyListener = (Boolean) -> Unit

class MediaSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicDataController: MusicDataController
) {
    var audioMediaItems: List<MediaItem> = arrayListOf()
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

    fun whenReady(listener: OnReadyListener): Boolean {
        return if (
            state == AudioSourceState.STATE_CREATED ||
            state == AudioSourceState.STATE_INITIALIZING
        ) {
            onReadyListeners += listener
            false
        } else {
            listener.invoke(isReady)
            true
        }
    }

    suspend fun load() {
        state = AudioSourceState.STATE_INITIALIZING
        musicDataController.listenCurrentMusicList().collect { list ->
            transformData(list)
        }
    }

    fun refresh() {
        onReadyListeners.clear()
        state = AudioSourceState.STATE_CREATED
    }

    @OptIn(UnstableApi::class)
    private fun transformData(data: List<TrackModel>) {
        audioMediaItems = data.map { audio ->
            buildMediaItem(audio)
        }
        state = AudioSourceState.STATE_INITIALIZED
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
//                    .setArtworkData(getImage(audio, context))
                    .setDurationMs(formatTimeToMls(audio.duration).toLong())
                    .build()
            )
            .build()
    }

    private fun getImage(audio: TrackModel, context: Context): ByteArray {
        var image: Bitmap? = null
        if (audio.image != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val downloadedImage = loadImageWithGlide(audio.image, context)
                withContext(Dispatchers.Main) {
                    image = downloadedImage
                }
            }
        } else if (audio.path != null) {
            image = loadPicture(audio.path)
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        if (image != null) {
            image?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.bg_sound)
                .compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        }
        return byteArrayOutputStream.toByteArray()
    }
}

    enum class AudioSourceState {

        STATE_CREATED,
        STATE_INITIALIZING,
        STATE_INITIALIZED,
        STATE_ERROR,
    }