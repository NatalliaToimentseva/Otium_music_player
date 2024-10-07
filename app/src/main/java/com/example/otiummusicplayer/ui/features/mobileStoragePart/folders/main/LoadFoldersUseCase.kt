package com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.main

import com.example.otiummusicplayer.appComponents.contentProviders.MobileStorageMusicProvider
import com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.main.domain.FoldersResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.MalformedURLException
import java.sql.SQLException
import javax.inject.Inject

class LoadFoldersUseCase @Inject constructor(
    private val contentProvider: MobileStorageMusicProvider
) {

    suspend fun loadFolders(): Flow<FoldersResult> {
        return flow {
            emit(FoldersResult.Loading)
            try {
                emit(FoldersResult.Success(contentProvider.getFolders()))
            } catch (e: SQLException) {
                emit(FoldersResult.Error(e.message ?: "SQLException"))
            } catch (e: MalformedURLException) {
                emit(FoldersResult.Error(e.message ?: "MalformedURLException"))
            }
        }
    }
}