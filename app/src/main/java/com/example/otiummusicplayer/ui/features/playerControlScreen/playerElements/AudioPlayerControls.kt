package com.example.otiummusicplayer.ui.features.playerControlScreen.playerElements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.PlayerTrackAction
import com.example.otiummusicplayer.ui.features.playerControlScreen.domain.PlayerTrackState
import com.example.otiummusicplayer.utils.formatTimeMls
import com.example.otiummusicplayer.utils.formatTimeToMls

@Composable
fun AudioPlayerControls(
    state: PlayerTrackState,
    processAction: (action: PlayerTrackAction) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = formatTimeMls(state.currentPosition),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = state.currentTrack?.duration ?: "00:00",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Slider(
            value = state.currentPosition.toFloat(),
            onValueChange = { value ->
                processAction(PlayerTrackAction.SetCurrentPosition(value.toInt()))
            },
            valueRange = 0f..formatTimeToMls(state.currentTrack?.duration).toFloat(),
            onValueChangeFinished = {
                processAction(PlayerTrackAction.SeekToPosition)
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onBackground,
                activeTrackColor = MaterialTheme.colorScheme.onBackground,
                disabledInactiveTrackColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 20.dp, bottom = 10.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 10.dp)
        ) {
            IconButton(onClick = {}) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_shuffle),
                    contentDescription = stringResource(id = R.string.btn_shuffle)
                )
            }
            IconButton(onClick = { processAction(PlayerTrackAction.PlayPrevious) }) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_prev),
                    contentDescription = stringResource(id = R.string.btn_previous)
                )
            }
            IconButton(
                onClick = {
                    if (state.isPlayed) {
                        processAction(PlayerTrackAction.Stop)
                    } else {
                        processAction(PlayerTrackAction.Play)
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            ) {
                Image(
                    imageVector = if (state.isPlayed) {
                        ImageVector.vectorResource(id = R.drawable.btn_pause)
                    } else {
                        ImageVector.vectorResource(id = R.drawable.btn_play)
                    },
                    contentDescription = stringResource(id = R.string.btn_play_stop)
                )
            }
            IconButton(onClick = { processAction(PlayerTrackAction.PlayNext) }) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_next),
                    contentDescription = stringResource(id = R.string.btn_next)
                )
            }
            IconButton(onClick = { processAction(PlayerTrackAction.LoopTrack) }) {
                Image(
                    imageVector = if (state.isPlayerLooping) {
                        ImageVector.vectorResource(id = R.drawable.btn_repeat_one)
                    } else {
                        ImageVector.vectorResource(id = R.drawable.btn_repeat)
                    },
                    contentDescription = stringResource(id = R.string.btn_repeat)
                )
            }
        }
    }
}