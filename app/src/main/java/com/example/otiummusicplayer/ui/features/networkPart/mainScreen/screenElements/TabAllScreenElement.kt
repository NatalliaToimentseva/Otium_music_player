package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.screenElements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.generalProjectsScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.generalProjectsScreenElements.TracksListElement
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.NetworkSearchState
import com.example.otiummusicplayer.utils.toast

@Composable
fun AllDataScreenElement(
    state: NetworkSearchState,
    processAction: (action: NetworkSearchAction) -> Unit,
    goTrackList: (id: String) -> Unit,
    goToPlayer: (itemID: String, tracks: String) -> Unit
) {
    val albums = state.albums?.collectAsLazyPagingItems()
    val artists = state.artists?.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp)
    ) {
        SearchFieldScreenElement(state, processAction)
        if (state.searchResult == null) {
            Column(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(id = R.string.albums),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(10.dp)
                )
                AlbumsList(data = albums, goTrackList)
                Text(
                    text = stringResource(id = R.string.artists),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(10.dp)
                )
                ArtistsList(artists = artists) { id ->
                    processAction(NetworkSearchAction.LoadAlbumsByArtist(id))
                }
            }
            if (state.isLoading) {
                ShowProgress()
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
            if (state.searchResult.collectAsLazyPagingItems().itemSnapshotList.isEmpty()) {
                Box(modifier = Modifier.padding(15.dp)) {
                    Text(text = "Data not found", color = MaterialTheme.colorScheme.primary)
                }
            } else {
                TracksListElement(
                    tracksList = state.searchResult,
                    goToPlayer = goToPlayer,
                    null, null,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .padding(bottom = 10.dp)
                )
            }
        }
    }
}