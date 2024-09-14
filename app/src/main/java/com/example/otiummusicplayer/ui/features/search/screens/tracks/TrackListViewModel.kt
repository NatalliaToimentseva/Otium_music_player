package com.example.otiummusicplayer.ui.features.search.screens.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otiummusicplayer.network.Api
import com.example.otiummusicplayer.network.entities.TrackData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackListViewModel @Inject constructor(
    private val api: Api
) : ViewModel() {

    val tracksData = MutableStateFlow<TrackData?>(null)

    fun getTrByAlb(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val trackResponse = api.getTracksByAlbum()
            val listTrackData = trackResponse.results.firstOrNull()
            tracksData.tryEmit(listTrackData)
        }
    }
}
