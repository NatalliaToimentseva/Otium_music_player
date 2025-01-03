package com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.generalProjectsScreenElements.BottomNavigationScreenElement
import com.example.otiummusicplayer.ui.features.generalProjectsScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.generalProjectsScreenElements.TracksListElement
import com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks.domain.MobileStorageTracksAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks.domain.MobileStorageTracksState
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog.PlaylistChoiceAlertDialogDestination
import com.example.otiummusicplayer.ui.features.playerControlScreen.MOBILE_TRACK
import com.example.otiummusicplayer.ui.navigation.Route
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun MobileStorageTracksScreenDestination(
    navHostController: NavHostController,
    viewModel: MobileStorageTracksScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    MobileStorageTracksScreen(
        state = state,
        processAction = viewModel::processAction,
        goToPlayer = { itemID: String, tracks: String ->
            navHostController.navigate(
                Route.PlayTrackScreen.selectRoute(
                    whereFrom = MOBILE_TRACK,
                    itemId = itemID,
                    tracks = tracks
                )
            )
        },
        { route ->
            navHostController.navigate(route)
        })
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
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
                Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = { },
                navigationIcon = {
                    if (state.selectedTracks.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = {
                                processAction(MobileStorageTracksAction.UnselectAllTracks)
                            }) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_back),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(
                                            top = 4.dp,
                                            end = 10.dp
                                        )
                                )
                            }
                            IconButton(onClick = {
                                processAction(MobileStorageTracksAction.ShowPlayListDialog)
                            }) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_add_playlist),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(
                                            top = 4.dp,
                                            end = 10.dp
                                        )
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationScreenElement(
                Route.MobileStorageTracksScreen,
                permissionsState,
                navigate
            )
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            state.storageTracks?.let { tracks ->
                TracksListElement(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .padding(bottom = 10.dp),
                    tracksList = tracks,
                    goToPlayer = goToPlayer,
                    onClick = { selectedItem ->
                        processAction(MobileStorageTracksAction.AddTrackToSelected(selectedItem))
                    },
                    selectedItem = state.selectedTracks
                )
            }
            if (state.isLoading) {
                ShowProgress()
            }
            if (state.isShowPlaylistsDialog) {
                PlaylistChoiceAlertDialogDestination(
                    selectedTracks = state.selectedTracks,
                    { processAction(MobileStorageTracksAction.ClosePlayListDialog) },
                    { processAction(MobileStorageTracksAction.UnselectAllTracks) }
                )
            }
        }
    }
}