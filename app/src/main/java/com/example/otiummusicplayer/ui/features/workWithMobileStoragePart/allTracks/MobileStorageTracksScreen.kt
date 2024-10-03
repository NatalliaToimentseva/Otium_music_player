package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.ui.features.generalScreenElements.BottomNavigationScreenElement
import com.example.otiummusicplayer.ui.features.generalScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.generalScreenElements.TracksListElement
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks.domain.MobileStorageTracksAction
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.allTracks.domain.MobileStorageTracksState
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.Graphite
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun MobileStorageTracksScreenDestination(
    navHostController: NavHostController,
    viewModel: MobileStorageTracksScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    MobileStorageTracksScreen(state = state, processAction = viewModel::processAction, goToPlayer = { itemID: String, tracks: String ->
        navHostController.navigate(
            Route.PlayTrackScreen.selectRoute(
                itemId = itemID,
                tracks = tracks
            )
        )
    }, { route ->
        navHostController.navigate(route)
    })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MobileStorageTracksScreen(
    state: MobileStorageTracksState,
    processAction: (MobileStorageTracksAction) -> Unit,
    goToPlayer: (itemID: String, tracks: String) -> Unit,
    navigate: (route: String) -> Unit
) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.READ_MEDIA_AUDIO,
            )
        } else {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    )
    LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            processAction(MobileStorageTracksAction.IsShowPermissionDialog(true))
        }
    }
    Scaffold(
        modifier = Modifier
            .background(Graphite)
            .fillMaxSize()
            .padding(all = 10.dp),
        containerColor = Graphite,
        bottomBar = {
            BottomNavigationScreenElement(
                Route.MobileStorageTracksScreen,
                permissionsState,
                navigate
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            state.storageTracks?.let { tracks ->
                TracksListElement(tracks, goToPlayer)
            }
            if (state.isLoading) {
                ShowProgress(null)
            }
        }
    }
}