package com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen.domain.NetworkSearchState
import com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen.screenElements.AllDataScreenElement
import com.example.otiummusicplayer.ui.features.generalScreenElements.BottomNavigationScreenElement
import com.example.otiummusicplayer.ui.features.generalScreenElements.TracksListElement
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

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
            navHostController.navigate(
                Route.PlayTrackScreen.selectRoute(
                    itemId = itemID,
                    tracks = tracks
                )
            )
        },
        { route ->
            navHostController.navigate(route)
        })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NetworkSearchScreen(
    state: NetworkSearchState,
    processAction: (action: NetworkSearchAction) -> Unit,
    goTrackList: (id: String) -> Unit,
    goToPlayer: (itemID: String, tracks: String) -> Unit,
    navigate: (route: String) -> Unit
) {
    val pagerState =
        rememberPagerState(initialPage = state.initialPage) { state.tabsName.size }
    val selectedTabIndex = remember {
        derivedStateOf { pagerState.currentPage }
    }
    val permissionsState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.READ_MEDIA_AUDIO,
            )
        } else {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(10.dp),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigationScreenElement(
                Route.NetworkSearch,
                permissionsState,
                navigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value]),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clip(RoundedCornerShape(5.dp))
            ) {
                state.tabsName.forEachIndexed { index, text ->
                    Tab(selected = selectedTabIndex.value == index,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = Color.DarkGray,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(text = text)
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1.0f),
            ) { page ->
                if (page == 0) {
                    AllDataScreenElement(state, processAction, goTrackList, goToPlayer)
                } else {
                    state.favoriteList?.let {
                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            TracksListElement(
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .padding(bottom = 10.dp),
                                tracksList = it,
                                goToPlayer = goToPlayer,
                                null, null
                            )
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
            {}, {}, { _, _ -> }, {}
        )
    }
}