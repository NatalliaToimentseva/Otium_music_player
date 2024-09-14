package com.example.otiummusicplayer.ui.features.playTrack.playerElements

import android.media.MediaPlayer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.playTrack.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.ui.theme.White
import kotlinx.coroutines.delay


@Composable
fun AudioPlayerControls(
    mediaPlayer: MediaPlayer,
    totalDurationMillis: Int?,
    processAction: (action: PlayerTrackAction) -> Unit,
) {
    var currentPosition by remember { mutableStateOf(0) }
    var isPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(mediaPlayer) {
        while (true) {
            currentPosition = mediaPlayer.currentPosition
            delay(1000)
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
                text = formatTime(currentPosition),
                color = White,
            )
            Text(
                text = formatTime(totalDurationMillis ?: 0),
                color = White,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Slider(
            value = currentPosition.toFloat(),
            onValueChange = { value ->
                currentPosition = value.toInt()
            },
            valueRange = 0f..(totalDurationMillis?.toFloat() ?: 0f),
            onValueChangeFinished = {
                mediaPlayer.seekTo(currentPosition)
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
                    if (isPlayed) {
                        processAction(PlayerTrackAction.Stop)
                        isPlayed = false
                    } else {
                        processAction(PlayerTrackAction.Play)
                        isPlayed = true
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            ) {
                Image(
                    imageVector = if (isPlayed && mediaPlayer.isPlaying) {
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

private fun formatTime(millis: Int): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    OtiumMusicPlayerTheme {
//        val mp = MediaPlayer()
//        fun AudioPlayerControls(
//            mediaPlayer: MediaPlayer = mp,
//            totalDurationMillis: Int = 100000,
//        ) {
//        }
//    }
//}