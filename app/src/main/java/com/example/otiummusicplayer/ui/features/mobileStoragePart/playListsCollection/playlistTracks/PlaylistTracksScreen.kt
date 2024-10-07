package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks

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
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks.domain.PlaylistTracksScreenAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks.domain.PlaylistTracksScreenState
import com.example.otiummusicplayer.ui.features.playerControlScreen.MOBILE_TRACK
import com.example.otiummusicplayer.ui.navigation.Route

@Composable
fun PlaylistTracksScreenDestination(
    playlistId: Long,
    navHostController: NavHostController,
    viewModel: PlaylistTracksScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    PlaylistTracksScreen(
        playlistId = playlistId,
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
        goBack = {
            navHostController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistTracksScreen(
    playlistId: Long,
    state: PlaylistTracksScreenState,
    processAction: (action: PlaylistTracksScreenAction) -> Unit,
    goToPlayer: (itemID: String, tracks: String) -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        processAction(PlaylistTracksScreenAction.LoadPlaylistTracks(playlistId))
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
                            if (state.playlistSelectedTracks.isNotEmpty()) {
                                processAction(PlaylistTracksScreenAction.UnselectAllTracks)
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
                        if (state.playlistSelectedTracks.isNotEmpty()) {
                            IconButton(onClick = {
                                processAction(PlaylistTracksScreenAction.DeleteTracksFromPlaylist)
                            }) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp)
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
            state.playlistTracks?.let {
                TracksListElement(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .padding(bottom = 10.dp),
                    tracksList = it,
                    goToPlayer = goToPlayer,
                    onClick = { selectedTrack ->
                        processAction(PlaylistTracksScreenAction.SelectTracks(selectedTrack))
                    },
                    selectedItem = state.playlistSelectedTracks
                )
            }
        }
    }
}