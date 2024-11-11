package com.example.otiummusicplayer.appComponents.services.downloadService

interface FileDownloader {

    fun downloadFile(url: String): Long
}