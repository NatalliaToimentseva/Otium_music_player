package com.example.otiummusicplayer.ui.features.search.tracks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.otiummusicplayer.ui.features.generalScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.search.tracks.domain.TrackLisState
import com.example.otiummusicplayer.ui.features.search.tracks.domain.TrackListAction
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.ui.theme.White
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
            Route.PlayTrackScreen.selectRoute(itemId = itemId, tracks = tracksList)
        )
    })
}

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

    Column(
        modifier = Modifier
            .background(Graphite)
            .fillMaxSize()
            .padding(20.dp)
    ) {
        IconButton(onClick = {
            goBack.invoke()
        }) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.btn_back),
                contentDescription = stringResource(id = R.string.btn_back),
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
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
                        fontSize = 20.sp,
                        color = White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        if (state.isLoading) {
            ShowProgress(null)
        }
        if (state.error != null) {
            LocalContext.current.toast(state.error)
            processAction(TrackListAction.ClearError)
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
                        "", "Test", "", "", 100, "", "",
                        "", "", false, ""
                    )
                )
            ),
            {}, {}, { _ ->
            }
        )
    }
}