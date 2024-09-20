package com.example.otiummusicplayer.ui.features.playTrack

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackState
import com.example.otiummusicplayer.ui.features.playTrack.playerElements.AudioPlayerControls
import com.example.otiummusicplayer.ui.theme.GradientTr
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.GraphiteTr
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.ui.theme.TealTr
import com.example.otiummusicplayer.utils.toast

@Composable
fun PlayTrackDestination(
    id: Int,
    itemId: String,
    navHostController: NavHostController,
    viewModel: PlayTrackViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    PlayTrackScreen(
        id = id,
        itemId = itemId,
        state = state,
        processAction = viewModel::processAction
    ) {
        navHostController.popBackStack()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayTrackScreen(
    id: Int,
    itemId: String,
    state: PlayerTrackState,
    processAction: (action: PlayerTrackAction) -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(id) {
        processAction(PlayerTrackAction.Init(id, itemId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = {
                        goBack.invoke()
                    }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.btn_back),
                            contentDescription = stringResource(id = R.string.btn_back),
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Graphite)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Graphite)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Graphite)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
            ) {
                AsyncImage(
                    model = state.currentTrack?.image,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image),
                    contentDescription = stringResource(id = R.string.album_im),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(410.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.verticalGradient(
                                0.05f to TealTr,
                                0.8f to GraphiteTr,
                                0.02f to Graphite,
                                startY = 70f,
                                endY = 0f
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                0.05f to GradientTr,
                                0.4f to GraphiteTr,
                                0.3f to Graphite,
                                startY = 0f,
                                endY = 210f
                            )
                        )
                )
            }
            Text(
                text = state.currentTrack?.name ?: stringResource(id = R.string.no_title),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        Brush.verticalGradient(
                            0.05f to GradientTr,
                            0.4f to GraphiteTr,
                            0.3f to Graphite,
                            startY = 0f,
                            endY = 210f
                        )
                    )
                    .padding(start = 20.dp, bottom = 10.dp)
            )
            AudioPlayerControls(state, processAction)
            if (state.isLoading) {
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
                processAction(PlayerTrackAction.ClearError)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayTrackScreenPreview() {
    OtiumMusicPlayerTheme {
        PlayTrackScreen(
            id = 1,
            itemId = "",
            state = PlayerTrackState(
                tracks = arrayListOf(
                    TrackModel(
                        "",
                        name = "Карев А.В.(VIGOR) - Время героев",
                        image = "https://cs13.pikabu.ru/post_img/big/2023/03/03/6/1677836243167140809.png",
                        "", 1000, "", "", "", "", false, ""
                    )
                )
            ),
            {}, {}
        )
    }
}