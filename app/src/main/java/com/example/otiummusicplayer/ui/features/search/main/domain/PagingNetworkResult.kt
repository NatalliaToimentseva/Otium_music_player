package com.example.otiummusicplayer.ui.features.search.main.domain

sealed class PagingNetworkResult<out T> {

    data class Success<T>(val data: List<T>) : PagingNetworkResult<T>()

    data class Error(val error: Throwable) : PagingNetworkResult<Nothing>()
}
