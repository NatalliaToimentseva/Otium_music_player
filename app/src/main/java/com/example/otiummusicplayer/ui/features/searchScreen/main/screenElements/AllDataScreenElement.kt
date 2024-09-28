package com.example.otiummusicplayer.ui.features.searchScreen.main.screenElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.generalScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.searchScreen.main.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.searchScreen.main.domain.NetworkSearchState
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.utils.toast
import com.google.gson.GsonBuilder

@Composable
fun AllDataScreenElement(
    state: NetworkSearchState,
    processAction: (action: NetworkSearchAction) -> Unit,
    goTrackList: (id: String) -> Unit,
    goToPlayer: (itemID: String, tracks: String) -> Unit
) {
    val albums = state.albums?.collectAsLazyPagingItems()
    val artists = state.artists?.collectAsLazyPagingItems()
    val searchResult = state.searchResult?.collectAsLazyPagingItems()

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(top = 15.dp)){
        SearchFieldScreenElement(state,processAction)
        if (state.searchResult == null) {
            Column(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(id = R.string.albums),
                    fontSize = 24.sp,
                    color = White,
                    modifier = Modifier
                        .padding(10.dp)
                )
                AlbumsList(data = albums, goTrackList)
                Text(
                    text = stringResource(id = R.string.artists),
                    fontSize = 24.sp,
                    color = White,
                    modifier = Modifier
                        .padding(10.dp)
                )
                ArtistsList(artists = artists) { id ->
                    processAction(NetworkSearchAction.LoadAlbumsByArtist(id))
                }
            }
            if (state.isLoading) {
                ShowProgress(null)
            }
            if (state.error != null) {
                LocalContext.current.toast(state.error)
                processAction(NetworkSearchAction.ClearError)
            }

            if (state.showDialog) {
                ShowArtistsAlbums(state.artistAlbums, goTrackList) {
                    processAction(NetworkSearchAction.HideDialog)
                }
            }
        } else {
            searchResult?.let { searchItems ->
                when (searchItems.loadState.refresh) {
                    is LoadState.Error -> LocalContext.current.toast(stringResource(id = R.string.loading_error))
                    LoadState.Loading -> ShowProgress(null)
                    else -> {
                        LazyColumn {
                            items(searchItems.itemCount) { index ->
                                Row(verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        val tracks =
                                            searchItems.itemSnapshotList as List<TrackModel>
                                        val gson = GsonBuilder().create()
                                        val tracksList = gson.toJson(tracks)
                                        searchItems[index]?.id?.let {
                                            goToPlayer.invoke(
                                                it,
                                                tracksList
                                            )
                                        }
                                    }) {
                                    AsyncImage(
                                        model = searchItems[index]?.image,
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
                                            text = searchItems[index]?.name
                                                ?: stringResource(id = R.string.no_title),
                                            fontSize = 20.sp,
                                            color = White,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                        Text(
                                            text = searchItems[index]?.artistName
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
        }
    }
}