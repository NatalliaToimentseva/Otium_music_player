package com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.generalScreenElements.TracksListElement
import com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks.domain.FoldersTracksScreenAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks.domain.FoldersTracksScreenState
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog.PlaylistChoiceAlertDialogDestination
import com.example.otiummusicplayer.ui.navigation.Route
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun FoldersTracksScreenDestination(
    folderId: Int,
    navHostController: NavHostController,
    viewModel: FoldersTracksScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    FoldersTracksScreen(
        folderId = folderId,
        state = state,
        processAction = viewModel::processAction,
        { itemID: String, tracks: String ->
            navHostController.navigate(
                Route.PlayTrackScreen.selectRoute(
                    itemId = itemID,
                    tracks = tracks
                )
            )
        }, {
            navHostController.popBackStack()
        })
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FoldersTracksScreen(
    folderId: Int,
    state: FoldersTracksScreenState,
    processAction: (action: FoldersTracksScreenAction) -> Unit,
    goToPlayer: (itemID: String, tracks: String) -> Unit,
    goBack: () -> Unit,
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
            processAction(FoldersTracksScreenAction.IsShowPermissionDialog(true))
        }
    }
    LaunchedEffect(Unit) {
        processAction(FoldersTracksScreenAction.LoadFolderTracks(folderId))
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = { },
                navigationIcon = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    {
                        IconButton(onClick = {
                            if (state.selectedTracks.isNotEmpty()) {
                                processAction(FoldersTracksScreenAction.UnselectAllTracks)
                            } else {
                                goBack.invoke()
                            }
                        }) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.btn_back),
                                contentDescription = stringResource(id = R.string.btn_back),
                                modifier = Modifier
                                    .size(35.dp)
                                    .padding(top = 4.dp, start = 10.dp)
                            )
                        }
                        if (state.selectedTracks.isNotEmpty()) {
                            IconButton(onClick = {
                                processAction(FoldersTracksScreenAction.ShowPlayListDialog)
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
        ) {
            state.tracksFolderList?.let {
                TracksListElement(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .padding(bottom = 10.dp),
                    tracksList = it,
                    goToPlayer = goToPlayer,
                    onClick = { selectedItem ->
                        processAction(FoldersTracksScreenAction.AddTrackToSelected(selectedItem))
                    },
                    selectedItem = state.selectedTracks
                )
            }
            if (state.isShowPlaylistsDialog) {
                PlaylistChoiceAlertDialogDestination(
                    selectedTracks = state.selectedTracks,
                    { processAction(FoldersTracksScreenAction.ClosePlayListDialog) },
                    { processAction(FoldersTracksScreenAction.UnselectAllTracks) }
                )
            }
        }
    }
}