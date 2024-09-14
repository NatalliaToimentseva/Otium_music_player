package com.example.otiummusicplayer.network.entities

data class SearchResult(
    val albums: List<String>,
    val artists: List<String>,
    val tracks: List<String>
)