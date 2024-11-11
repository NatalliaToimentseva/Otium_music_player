package com.example.otiummusicplayer.ui.features.generalProjectsScreenElements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.theme.FloatingButton
import com.example.otiummusicplayer.ui.theme.HoverLight
import com.example.otiummusicplayer.ui.theme.TealExtraLight
import com.example.otiummusicplayer.ui.theme.TealTr
import com.example.otiummusicplayer.utils.loadPicture
import com.example.otiummusicplayer.utils.toStringGsonFormat
import com.example.otiummusicplayer.utils.toast
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TracksListElement(
    tracksList: Flow<PagingData<TrackModel>>,
    goToPlayer: (itemID: String, tracks: String) -> Unit,
    onClick: ((item: TrackModel) -> Unit)?,
    selectedItem: List<TrackModel>?,
    modifier: Modifier = Modifier
) {
    val tracks = tracksList.collectAsLazyPagingItems()
    when (tracks.loadState.refresh) {
        is LoadState.Error -> LocalContext.current.toast(stringResource(id = R.string.loading_error))
        LoadState.Loading -> ShowProgress()
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp, bottom = 20.dp)
            ) {
                items(tracks.itemCount) { index ->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp)
                            .background(
                                if (selectedItem?.contains(tracks[index]) == true) {
                                    Brush.horizontalGradient(
                                        arrayListOf(
                                            TealExtraLight,
                                            TealTr,
                                            FloatingButton,
                                            HoverLight,
                                            HoverLight,
                                            HoverLight,
                                            FloatingButton,
                                            TealTr,
                                            TealExtraLight
                                        )
                                    )
                                } else Brush.horizontalGradient(
                                    arrayListOf(
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                            .combinedClickable(
                                onClick = {
                                    tracks[index]?.id?.let {
                                        goToPlayer.invoke(
                                            it,
                                            tracks.itemSnapshotList.toStringGsonFormat()
                                        )
                                    }
                                },
                                onLongClick = {
                                    tracks[index]?.let { onClick?.invoke(it) }
                                }
                            )) {
                        Row(modifier = Modifier.weight(0.82f)) {
                            AsyncImage(
                                model = if (tracks[index]?.image != null) {
                                    tracks[index]?.image
                                } else {
                                    loadPicture(tracks[index]?.path)
                                },
                                placeholder = painterResource(id = R.drawable.no_image),
                                error = painterResource(id = R.drawable.no_image),
                                contentDescription = null,
                                modifier = modifier
                            )
                            Column {
                                Text(
                                    text = tracks[index]?.name
                                        ?: stringResource(id = R.string.no_title),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                                Text(
                                    text = tracks[index]?.artistName
                                        ?: stringResource(id = R.string.no_title),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        tracks[index]?.duration?.let {
                            Text(
                                text = it,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                                    .weight(0.18f)
                            )
                        }
                    }
                }
            }
        }
    }
}