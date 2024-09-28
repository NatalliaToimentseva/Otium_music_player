package com.example.otiummusicplayer.utils

interface FileDownloader {

    fun downloadFile(url: String): Long
}