package com.example.otiummusicplayer.ui.features.search.screens.main.domain


sealed class NetworkResult {

    data class Success<T: Any>(val data: List<T>) : NetworkResult()

    data class Error(val throwable: Throwable) : NetworkResult()
}