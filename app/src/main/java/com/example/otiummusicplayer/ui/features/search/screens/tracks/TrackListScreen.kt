package com.example.otiummusicplayer.ui.features.search.screens.tracks

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.network.entities.TrackData
import com.example.otiummusicplayer.network.entities.TrackX
import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackLisState
import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackListAction
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.utils.toast

@Composable
fun TrackListDestination(
    id: String,
    navHostController: NavHostController,
    viewModel: TrackListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    TrackListScreen(id = id, state = state, processAction = viewModel::processAction, {
        navHostController.popBackStack(
//            route = "NetworkSearchScreen",
//            inclusive = false
        )
    }, { imgUrl, title, audioUrl ->
        navHostController.navigate(
            "PlayTrackScreen?imgUrl=${Uri.encode(imgUrl)}&title=${Uri.encode(title)}&audioUrl=${
                Uri.encode(
                    audioUrl
                )
            }"
        )
    })
}

@Composable
fun TrackListScreen(
    id: String,
    state: TrackLisState,
    processAction: (action: TrackListAction) -> Unit,
    goBack: () -> Unit,
    navigate: (
        imgUrl: String?,
        title: String?,
        audioURL: String?,
    ) -> Unit
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
                contentDescription = "Back button",
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
        LazyColumn {
            items(items = state.tracksData?.tracks ?: arrayListOf()) { item ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navigate.invoke(state.tracksData?.image, item.name, item.audio)
                    }) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
                        contentDescription = "Play composition",
                    )
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        color = White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        if (state.isLoading == true) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(50.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
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
                tracksData = TrackData(
                    artist_id = "",
                    artist_name = "",
                    id = "",
                    image = "",
                    name = "",
                    releasedate = "",
                    tracks = (arrayListOf(
                        TrackX(
                            audio = "",
                            audiodownload = "",
                            audiodownload_allowed = true,
                            duration = "",
                            id = "",
                            license_ccurl = "",
                            name = "Test",
                            position = ""
                        ),
                        TrackX(
                            audio = "",
                            audiodownload = "",
                            audiodownload_allowed = true,
                            duration = "",
                            id = "",
                            license_ccurl = "",
                            name = "Test2",
                            position = ""
                        )
                    )),
                    zip = "",
                    zip_allowed = true
                )
            ),
            {}, {}, { imgUrl, title, audioUrl ->
            }
        )
    }
}