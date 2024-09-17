package com.example.otiummusicplayer.ui.features.search.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkSearchState
import com.example.otiummusicplayer.ui.features.search.screens.main.screenElements.AlbumsList
import com.example.otiummusicplayer.ui.features.search.screens.main.screenElements.ArtistsList
import com.example.otiummusicplayer.ui.features.search.screens.main.screenElements.ShowArtistsAlbums
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.Hover
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.utils.toast

@Composable
fun NetworkSearchDestination(
    navHostController: NavHostController,
    viewModel: NetworkSearchScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    NetworkSearchScreen(state = state, processAction = viewModel::processAction) { id ->
        navHostController.navigate("TrackList/$id")
    }
}

@Composable
fun NetworkSearchScreen(
    state: NetworkSearchState,
    processAction: (action: NetworkSearchAction) -> Unit,
    goTrackList: (id: String) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        processAction(NetworkSearchAction.LoadInitialData)
    }
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
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                value = "Search",
                onValueChange = { },
                leadingIcon = {
                    IconButton(onClick = { }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                            contentDescription = null
                        )
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedContainerColor = Graphite,
                    unfocusedContainerColor = Graphite,
                    cursorColor = White,
                    focusedIndicatorColor = White,
                    focusedLabelColor = White
                )
            )
            Text(
                text = "Albums",
                fontSize = 24.sp,
                color = White,
                modifier = Modifier
                    .padding(10.dp)
            )
            AlbumsList(data = state.albums.results, goTrackList)
            Text(
                text = "Artists",
                fontSize = 24.sp,
                color = White,
                modifier = Modifier
                    .padding(10.dp)
            )
            ArtistsList(artists = state.artists.results) { id ->
                processAction(NetworkSearchAction.LoadAlbumsByArtist(id))
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(50.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
            if (state.error != null) {
                LocalContext.current.toast(state.error)
                processAction(NetworkSearchAction.ClearError)
            }
        }
    }

    if (state.showDialog) {
        ShowArtistsAlbums(state.artistAlbums, goTrackList) {
            processAction(NetworkSearchAction.HideDialog)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkSearchScreenPreview() {
    OtiumMusicPlayerTheme {
        NetworkSearchScreen(
            state = NetworkSearchState(),
            {}, {}
        )
    }
}