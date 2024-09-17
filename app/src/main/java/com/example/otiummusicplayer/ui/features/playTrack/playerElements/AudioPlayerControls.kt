package com.example.otiummusicplayer.ui.features.playTrack.playerElements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackState
import com.example.otiummusicplayer.ui.theme.White
import kotlinx.coroutines.delay


@Composable
fun AudioPlayerControls(
    state: PlayerTrackState?,
    processAction: (action: PlayerTrackAction) -> Unit,
) {
//    var currentPosition by remember { mutableIntStateOf(0) }

    LaunchedEffect(state?.mediaPlayer) {
        state?.mediaPlayer?.let {
            while (true) {
                processAction(PlayerTrackAction.SetCurrentPosition(it.currentPosition))
                delay(1000)
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = formatTime(state?.currentPosition),
                color = White,
            )
            Text(
                text = formatTime(state?.duration ?: 0),
                color = White,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Slider(
            value = state?.currentPosition?.toFloat() ?: 0f,
            onValueChange = { value ->
                processAction(PlayerTrackAction.SetCurrentPosition(value.toInt()))
            },
            valueRange = 0f..(state?.duration?.toFloat() ?: 0f),
            onValueChangeFinished = {
                state?.let {
                    it.mediaPlayer?.seekTo(it.currentPosition)
                }
            },
            colors = SliderDefaults.colors(
                thumbColor = White,
                activeTrackColor = White,
                inactiveTrackColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            IconButton(onClick = {}) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_shuffle),
                    contentDescription = "Shuffle"
                )
            }
            IconButton(onClick = {}) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_prev),
                    contentDescription = "Previous"
                )
            }
            IconButton(
                onClick = {
                    if (state?.isPlayed == true) {
                        processAction(PlayerTrackAction.Stop)
                        processAction(PlayerTrackAction.SetPlayed(false))
                    } else {
                        processAction(PlayerTrackAction.Play)
                        processAction(PlayerTrackAction.SetPlayed(true))
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            ) {
                Image(
                    imageVector = if (state?.isPlayed == true && state.mediaPlayer?.isPlaying == true) {
                        ImageVector.vectorResource(id = R.drawable.btn_pause)
                    } else {
                        ImageVector.vectorResource(id = R.drawable.btn_play)
                    },
                    contentDescription = "Play/Stop"
                )
            }
            IconButton(onClick = {}) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_next),
                    contentDescription = "Next"
                )
            }
            IconButton(onClick = {}) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_repeat),
                    contentDescription = "Repeat"
                )
            }
        }
    }
}

private fun formatTime(millis: Int?): String {
    return if (millis != null) {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        "%02d:%02d".format(minutes, seconds)
    } else "0"
}