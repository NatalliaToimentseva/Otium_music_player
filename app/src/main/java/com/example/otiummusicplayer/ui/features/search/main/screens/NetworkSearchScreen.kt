package com.example.otiummusicplayer.ui.features.search.main.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.generalScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.search.main.domain.EMPTY
import com.example.otiummusicplayer.ui.features.search.main.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.search.main.domain.NetworkSearchState
import com.example.otiummusicplayer.ui.features.search.main.screenElements.AlbumsList
import com.example.otiummusicplayer.ui.features.search.main.screenElements.ArtistsList
import com.example.otiummusicplayer.ui.features.search.main.screenElements.ShowArtistsAlbums
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.ErrorRed
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.Hover
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.utils.toast
import com.google.gson.GsonBuilder

@Composable
fun NetworkSearchDestination(
    navHostController: NavHostController,
    viewModel: NetworkSearchScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    NetworkSearchScreen(state = state, processAction = viewModel::processAction,
        { id ->
            navHostController.navigate(Route.TrackList.selectRoute(id))
        },
        { itemID: String, tracks: String ->
            navHostController.navigate(Route.PlayTrackScreen.selectRoute(itemId = itemID, tracks = tracks))
        })
}

@Composable
fun NetworkSearchScreen(
    state: NetworkSearchState,
    processAction: (action: NetworkSearchAction) -> Unit,
    goTrackList: (id: String) -> Unit,
    goToPlayer: (itemID: String, tracks: String) -> Unit
) {
    val albums = state.albums?.collectAsLazyPagingItems()
    val artists = state.artists?.collectAsLazyPagingItems()
    val searchResult = state.searchResult?.collectAsLazyPagingItems()

    val selectedIcon by remember {
        mutableStateOf("Home")
    }

    Scaffold(
        modifier = Modifier
            .background(Graphite)
            .fillMaxSize()
            .padding(10.dp),
        containerColor = Graphite,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                containerColor = Graphite,
                contentColor = White
            ) {
                NavigationBarItem(
                    alwaysShowLabel = false,
                    selected = selectedIcon == "Home",
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = null,
                            modifier = Modifier.width(40.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Hover,
                        selectedIconColor = Graphite,
                        unselectedIconColor = White
                    )
                )
                NavigationBarItem(
                    alwaysShowLabel = false,
                    selected = selectedIcon == "Other",
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.width(40.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Hover,
                        selectedIconColor = Graphite,
                        unselectedIconColor = White
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                placeholder = {
                    Text(
                        text = if (state.isSearchError) {
                            stringResource(id = R.string.searchError)
                        } else stringResource(id = R.string.search)
                    )
                },
                value = state.searchValue,
                onValueChange = { value ->
                    processAction(NetworkSearchAction.SetSearchValue(value))
                },
                leadingIcon = {
                    IconButton(onClick = { processAction(NetworkSearchAction.SearchByQuery) }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    if (state.searchValue.isNotEmpty()) {
                        IconButton(onClick = {
                            processAction(NetworkSearchAction.SetSearchValue(EMPTY))
                            processAction(NetworkSearchAction.ClearSearchResult)
                        }) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.btn_close),
                                contentDescription = null
                            )
                        }
                    }
                },
                isError = state.isSearchError,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = White,
                    errorTextColor = ErrorRed,
                    unfocusedTextColor = White,
                    focusedContainerColor = Graphite,
                    errorContainerColor = Graphite,
                    unfocusedContainerColor = Graphite,
                    cursorColor = White,
                    focusedIndicatorColor = White
                )
            )
            if (state.searchResult == null) {
                Column(
                    modifier = Modifier
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
                    ShowProgress(modifier = Modifier.align(Alignment.CenterHorizontally))
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
                                            val tracks = searchItems.itemSnapshotList as List<TrackModel>
                                            val gson = GsonBuilder().create()
                                            val tracksList = gson.toJson(tracks)
                                            searchItems[index]?.id?.let { goToPlayer.invoke(it, tracksList) }
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
                                                text = searchItems[index]?.name ?: stringResource(id = R.string.no_title),
                                                fontSize = 20.sp,
                                                color = White,
                                                modifier = Modifier.padding(5.dp)
                                            )
                                            Text(
                                                text = searchItems[index]?.artistName ?: stringResource(id = R.string.no_title),
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
}

@Preview(showBackground = true)
@Composable
fun NetworkSearchScreenPreview() {
    OtiumMusicPlayerTheme {
        NetworkSearchScreen(
            state = NetworkSearchState(),
            {}, {}, {_,_ ->}
        )
    }
}