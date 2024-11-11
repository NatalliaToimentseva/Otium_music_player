package com.example.otiummusicplayer.ui.features.playerControlScreen.domain

import com.example.otiummusicplayer.appComponents.services.downloadService.FileDownloader
import javax.inject.Inject

class DownloadTrackUseCase @Inject constructor(
    private val fileDownloader: FileDownloader
) {

    fun downloadTrack(url:String): Long {
        return fileDownloader.downloadFile(url)
    }
}