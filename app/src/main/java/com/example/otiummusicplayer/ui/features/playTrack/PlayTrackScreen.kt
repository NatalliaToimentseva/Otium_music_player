package com.example.otiummusicplayer.ui.features.playTrack

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackState
import com.example.otiummusicplayer.ui.features.playTrack.playerElements.AudioPlayerControls
import com.example.otiummusicplayer.ui.theme.GradientTr
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.GraphiteTr
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.ui.theme.TealTr

//TEST
const val URL =
    "https://prod-1.storage.jamendo.com/?trackid=1467858&format=mp31&from=QeBfVL5FlSP5h7JRU1OYQQ%3D%3D%7C5OZLVhPfv%2Ff9Z7jFE1nvDw%3D%3D"

@Composable
fun PlayTrackDestination(
    navHostController: NavHostController,
    imgUrl: String?,
    title: String?,
    audioURL: String?,
    viewModel: PlayTrackViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    PlayTrackScreen(
        state = state,
        imgUrl = imgUrl,
        title = title,
        audioURL = audioURL,
        processAction = viewModel::processAction
    ) {
        navHostController.popBackStack()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayTrackScreen(
    state: PlayerTrackState?,
    imgUrl: String?,
    title: String?,
    audioURL: String?,
    processAction: (action: PlayerTrackAction) -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        processAction(PlayerTrackAction.SetUrl(audioURL ?: URL))
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
                            contentDescription = "Back",
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
                    model = imgUrl,
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
                text = title ?: "No title",
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayTrackScreenPreview() {
    OtiumMusicPlayerTheme {
        PlayTrackScreen(
            state = PlayerTrackState(),
            imgUrl = "https://cs13.pikabu.ru/post_img/big/2023/03/03/6/1677836243167140809.png",
            title = "Карев А.В.(VIGOR) - Время героев",
            audioURL = "",
            {},{}
        )
    }
}