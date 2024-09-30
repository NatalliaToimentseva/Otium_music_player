package com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen.domain

sealed class PagingResult<out T> {

    data class Success<T>(val data: List<T>) : PagingResult<T>()

    data class Error(val error: Throwable) : PagingResult<Nothing>()
}
