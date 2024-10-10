package com.example.otiummusicplayer.roomDB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.formatTimeMls

@Entity(
    tableName = "Tracks",
    indices = [
        Index("id", unique = true)
    ]
)
data class TracksDbEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("db_id")
    val dbId: Long,
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("track_name")
    val trackName: String,
    @ColumnInfo("track_image")
    val trackImage: String?,
    @ColumnInfo("track_path")
    val trackPath: String?,
    @ColumnInfo("track_audio")
    val trackAudio: String,
    @ColumnInfo("duration")
    val duration: Int,
    @ColumnInfo("album_name")
    val albumName: String,
    @ColumnInfo("album_id")
    val albumId: String?,
    @ColumnInfo("artist_name")
    val artistName: String,
    @ColumnInfo("audio_download")
    val audioDownload: String?,
    @ColumnInfo("is_download_allowed")
    val isDownloadAllowed: Boolean?,
    @ColumnInfo("share_url")
    val shareUrl: String?,
    @ColumnInfo("favorite")
    val isFavorite: Boolean?,
    @ColumnInfo("playlist_id")
    val playlistId: Long
)

fun TracksDbEntity.toTrackModel(): TrackModel {
    return TrackModel(
        id = id,
        name = trackName,
        image = trackImage,
        audio = trackAudio,
        path = trackPath,
        duration = formatTimeMls(duration),
        albumName = albumName,
        albumId = albumId,
        artistName = artistName,
        audioDownload = audioDownload,
        isDownloadAllowed = isDownloadAllowed,
        shareUrl = shareUrl,
        isFavorite = isFavorite,
        playlistId = playlistId
    )
}

fun List<TracksDbEntity>.toListTrackModels(): List<TrackModel> {
    return this.map { item -> item.toTrackModel() }
}
