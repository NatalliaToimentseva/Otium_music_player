package com.example.otiummusicplayer.appComponents.contentProviders.contentProvidersImpl

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.otiummusicplayer.appComponents.contentProviders.MobileStorageMusicProvider
import com.example.otiummusicplayer.models.mobilePart.MobileStorageTrackModel
import com.example.otiummusicplayer.models.mobilePart.TracksFolders
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MobileStorageMusicProviderImpl @Inject constructor(
    @ApplicationContext context: Context
) : MobileStorageMusicProvider {
    private val contentResolver = context.contentResolver
    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.BUCKET_ID,
        MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Audio.Media.DATA
    )

    @SuppressLint("Range")
    override suspend fun getAllStorageTracks(
        limit: Int,
        offset: Int
    ): Set<MobileStorageTrackModel> {
        val setOfTracks = mutableSetOf<MobileStorageTrackModel>()
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} = ?"
        val selectionArguments = arrayOf("1")
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArguments,
            null
        )?.use { cursor ->
            cursor.moveToPosition(offset)
            for (i in 0 until limit) {
                if (!cursor.moveToNext()) {
                    break
                } else {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val name =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val album =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artist =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val duration =
                        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val dirId =
                        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                    setOfTracks.add(
                        MobileStorageTrackModel(
                            id, title, name, album, artist, duration, dirId, path
                        )
                    )
                }
            }
        }
        return setOfTracks
    }

    @SuppressLint("Range")
    override suspend fun getFolders(): Set<TracksFolders> {
        val folders = mutableSetOf<TracksFolders>()
        val projection = arrayOf(
            MediaStore.Audio.Media.BUCKET_ID,
            MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
        )
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val dirId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))
                val dir =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))
                folders.add(
                    TracksFolders(
                        dirId, dir
                    )
                )
            }
        }
        return folders
    }

    @SuppressLint("Range")
    override suspend fun getTracksByFolderId(
        folderId: Int,
        limit: Int,
        offset: Int
    ): List<MobileStorageTrackModel> {
        val listOfFoldersTracks = mutableListOf<MobileStorageTrackModel>()
        val selection = "${MediaStore.Audio.Media.BUCKET_ID} = ?"
        val selectionArguments = arrayOf("$folderId")
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArguments,
            null
        )?.use { cursor ->
            cursor.moveToPosition(offset)
            for (i in 0 until limit) {
                if (!cursor.moveToNext()) {
                    break
                } else {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val name =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val album =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artist =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val duration =
                        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val dirId =
                        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                    listOfFoldersTracks.add(
                        MobileStorageTrackModel(
                            id, title, name, album, artist, duration, dirId, path
                        )
                    )
                }
            }
        }
        return listOfFoldersTracks
    }
}