package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks.domain

import com.example.otiummusicplayer.models.TrackModel

sealed class PlaylistTracksScreenAction {

    data class LoadPlaylistTracks(val playlistId: Long) : PlaylistTracksScreenAction()

    data class SelectTracks(val track: TrackModel) : PlaylistTracksScreenAction()

    data object UnselectAllTracks : PlaylistTracksScreenAction()

    data object DeleteTracksFromPlaylist : PlaylistTracksScreenAction()
}