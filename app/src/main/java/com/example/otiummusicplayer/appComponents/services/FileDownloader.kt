package com.example.otiummusicplayer.appComponents.services

interface FileDownloader {

    fun downloadFile(url: String): Long
}