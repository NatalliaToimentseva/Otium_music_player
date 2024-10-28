package com.example.otiummusicplayer.appComponents.services.downloadService

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.webkit.URLUtil
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val MIME_TYPE = "audio/mpeg"

class FileDownloaderImpl @Inject constructor(
    @ApplicationContext context: Context
) : FileDownloader {

    private val downloadManage = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val fileTitle = URLUtil.guessFileName(url, null, null)
        val request = DownloadManager.Request(url.toUri())
            .setMimeType(MIME_TYPE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            .setTitle(fileTitle)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileTitle)
        return downloadManage.enqueue(request)
    }
}