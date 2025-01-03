package com.example.otiummusicplayer.ui.features.networkPart.tracksScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.generalProjectsScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.networkPart.tracksScreen.domain.TrackLisState
import com.example.otiummusicplayer.ui.features.networkPart.tracksScreen.domain.TrackListAction
import com.example.otiummusicplayer.ui.features.playerControlScreen.NETWORK_TRACK
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.utils.toast
import com.google.gson.GsonBuilder

@Composable
fun TrackListDestination(
    id: String,
    navHostController: NavHostController,
    viewModel: TrackListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val gson = GsonBuilder().create()
    val tracks = state.tracks ?: arrayListOf()
    val tracksList = gson.toJson(tracks)
    TrackListScreen(id = id, state = state, processAction = viewModel::processAction, {
        navHostController.popBackStack()
    }, { itemId ->
        navHostController.navigate(
            Route.PlayTrackScreen.selectRoute(whereFrom = NETWORK_TRACK, itemId = itemId, tracks = tracksList)
        )
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackListScreen(
    id: String,
    state: TrackLisState,
    processAction: (action: TrackListAction) -> Unit,
    goBack: () -> Unit,
    navigate: (itemId: String) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        processAction(TrackListAction.Init(id))
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = { },
                navigationIcon = {
                    IconButton(onClick = {
                        goBack.invoke()
                    }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.btn_back),
                            contentDescription = stringResource(id = R.string.btn_back),
                            modifier = Modifier.padding(top = 4.dp, start = 10.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            LazyColumn {
                items(items = state.tracks ?: arrayListOf()) { item ->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            navigate.invoke(item.id)
                        }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
                            contentDescription = stringResource(id = R.string.composition),
                        )
                        Text(
                            text = item.name,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
            if (state.isLoading) {
                ShowProgress()
            }
            if (state.error != null) {
                LocalContext.current.toast(state.error)
                processAction(TrackListAction.ClearError)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackListPreview() {
    OtiumMusicPlayerTheme {
        TrackListScreen(
            id = "1",
            state = TrackLisState(
                tracks = arrayListOf(
                    TrackModel(
                        "", "Test", "", "", "","01:00", "", "",
                        "", "", false, "", false, -1
                    )
                )
            ),
            {}, {}, { _ ->
            }
        )
    }
}