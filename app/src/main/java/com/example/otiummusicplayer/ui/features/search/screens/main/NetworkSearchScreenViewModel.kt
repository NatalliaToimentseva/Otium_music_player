package com.example.otiummusicplayer.ui.features.search.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.network.Api
import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkSearchScreenViewModel @Inject constructor(
    private val api: Api
) : ViewModel() {

    val albums = MutableStateFlow(Album(results = arrayListOf()))
    val artists = MutableStateFlow(Artists(results = arrayListOf()))
    val artistAlbums = MutableStateFlow(Album(results = arrayListOf()))
    val showDialog = MutableStateFlow(false)

    private fun showDialog() {
        showDialog.tryEmit(true)
    }

    fun closeDialog() {
        showDialog.tryEmit(false)
    }

    fun getAlb() {
        viewModelScope.launch(Dispatchers.IO) {
            albums.tryEmit(api.getAlbums())
        }
    }

    fun getArt() {
        viewModelScope.launch(Dispatchers.IO) {
            artists.tryEmit(api.getArtists())
        }
    }

    fun getAlbumsByArtist(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            artistAlbums.tryEmit(api.getAlbumsByArtist())
            showDialog()
        }
    }
}