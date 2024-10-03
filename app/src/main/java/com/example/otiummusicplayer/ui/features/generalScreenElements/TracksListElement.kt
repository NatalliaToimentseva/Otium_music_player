package com.example.otiummusicplayer.ui.features.generalScreenElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.otiummusicplayer.models.networkPart.TrackModel
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.utils.toast
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow

@Composable
fun TracksListElement(
    tracksList: Flow<PagingData<TrackModel>>,
    goToPlayer: (itemID: String, tracks: String) -> Unit
) {
    val tracks = tracksList.collectAsLazyPagingItems()
    when (tracks.loadState.refresh) {
        is LoadState.Error -> LocalContext.current.toast(stringResource(id = R.string.loading_error))
        LoadState.Loading -> ShowProgress(null)
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                items(tracks.itemCount) { index ->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            val gson = GsonBuilder().create()
                            val convertedToStringList = gson.toJson(tracks.itemSnapshotList as List<TrackModel>)
                            tracks[index]?.id?.let {
                                goToPlayer.invoke(
                                    it,
                                    convertedToStringList
                                )
                            }
                        }) {
                        AsyncImage(
                            model = tracks[index]?.image,
                            placeholder = painterResource(id = R.drawable.no_image),
                            error = painterResource(id = R.drawable.no_image),
                            contentDescription = null,
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .padding(bottom = 10.dp),
                        )
                        Column {
                            Text(
                                text = tracks[index]?.name
                                    ?: stringResource(id = R.string.no_title),
                                fontSize = 20.sp,
                                color = White,
                                modifier = Modifier.padding(5.dp)
                            )
                            Text(
                                text = tracks[index]?.artistName
                                    ?: stringResource(id = R.string.no_title),
                                fontSize = 20.sp,
                                color = White,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}