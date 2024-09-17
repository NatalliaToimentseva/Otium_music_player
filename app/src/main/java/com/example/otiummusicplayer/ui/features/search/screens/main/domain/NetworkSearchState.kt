package com.example.otiummusicplayer.ui.features.search.screens.main.domain

import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists

data class NetworkSearchState(
    val albums: Album = Album(results = arrayListOf()),
    val artists: Artists = Artists(results = arrayListOf()),
    val artistAlbums: Album = Album(results = arrayListOf()),
    val showDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
