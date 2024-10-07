package com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog.domain.PlaylistChoiceAlertDialogAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playlistChoiceAlertDialog.domain.PlaylistChoiceAlertDialogState
import com.example.otiummusicplayer.ui.theme.FloatingButton
import com.example.otiummusicplayer.ui.theme.Hover
import com.example.otiummusicplayer.ui.theme.HoverLight
import com.example.otiummusicplayer.ui.theme.TealLight
import com.example.otiummusicplayer.ui.theme.TealTr

@Composable
fun PlaylistChoiceAlertDialogDestination(
    selectedTracks: List<TrackModel>,
    closeDialog: () -> Unit,
    unselectTracks: () -> Unit,
    viewModel: PlaylistChoiceAlertDialogViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    PlaylistChoiceAlertDialog(
        selectedTracks = selectedTracks,
        state = state,
        processAction = viewModel::processAction,
        closeDialog = closeDialog,
        unselectTracks = unselectTracks
    )
}

@Composable
fun PlaylistChoiceAlertDialog(
    selectedTracks: List<TrackModel>,
    state: PlaylistChoiceAlertDialogState,
    processAction: (action: PlaylistChoiceAlertDialogAction) -> Unit,
    closeDialog: () -> Unit,
    unselectTracks: () -> Unit
) {
    LaunchedEffect(Unit) {
        processAction(PlaylistChoiceAlertDialogAction.SetSelectedTracks(selectedTracks))
    }
    val playLists = state.allPlayLists?.collectAsLazyPagingItems()
    AlertDialog(
        onDismissRequest = { closeDialog.invoke() },
        confirmButton = {
            TextButton(
                onClick = {
                    processAction(PlaylistChoiceAlertDialogAction.AddTracksToPlayList)
                    unselectTracks.invoke()
                    closeDialog.invoke()
                },
                enabled = state.selectedPlayList != null,
                colors = ButtonColors(
                    contentColor = Hover,
                    disabledContentColor = Color.LightGray,
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Text(
                    text = stringResource(id = R.string.positive_button),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { closeDialog.invoke() }) {
                Text(
                    text = stringResource(id = R.string.negative_button),
                    color = Hover
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.playlist_dialog_title),
                    style = TextStyle(fontSize = 18.sp),
                    color = Hover
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)
                        .selectableGroup()
                ) {
                    playLists?.run {
                        items(itemCount) { index ->
                            playLists[index]?.let { list ->
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (list.id == state.selectedPlayList?.id) {
                                                Brush.horizontalGradient(
                                                    arrayListOf(
                                                        TealLight,
                                                        TealTr,
                                                        FloatingButton,
                                                        HoverLight,
                                                        FloatingButton,
                                                        TealTr,
                                                        TealLight
                                                    )
                                                )
                                            } else Brush.horizontalGradient(
                                                arrayListOf(
                                                    Color.Transparent,
                                                    Color.Transparent
                                                )
                                            )
                                        )
                                        .clickable {
                                            processAction(
                                                PlaylistChoiceAlertDialogAction.SelectPlayListFromDialog(
                                                    list
                                                )
                                            )
                                        }
                                ) {
                                    RadioButton(
                                        selected = list.id == state.selectedPlayList?.id,
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = MaterialTheme.colorScheme.secondary,
                                            unselectedColor = MaterialTheme.colorScheme.secondary
                                        ),
                                        onClick = {
                                            processAction(
                                                PlaylistChoiceAlertDialogAction.SelectPlayListFromDialog(
                                                    list
                                                )
                                            )
                                        })
                                    Text(
                                        text = list.playListTitle,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = TealLight
    )
}


