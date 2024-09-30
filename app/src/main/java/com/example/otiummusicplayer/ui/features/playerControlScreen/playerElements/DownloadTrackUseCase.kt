package com.example.otiummusicplayer.ui.features.playerControlScreen.playerElements

import com.example.otiummusicplayer.utils.FileDownloader
import javax.inject.Inject

class DownloadTrackUseCase @Inject constructor(
    private val fileDownloader: FileDownloader
) {

    fun downloadTrack(url:String): Long {
        return fileDownloader.downloadFile(url)
    }
}