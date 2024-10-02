package com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen.screenElements.FavoriteScreenElement
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.OtiumMusicPlayerTheme
import com.example.otiummusicplayer.ui.theme.White
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
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .background(Graphite)
            .fillMaxSize()
            .padding(10.dp),
        containerColor = Graphite,
        bottomBar = { BottomNavigationScreenElement(Route.NetworkSearch, navigate) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                containerColor = Graphite,
                contentColor = White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value]),
                        color = White
                    )
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clip(RoundedCornerShape(5.dp))
            ) {
                state.tabsName.forEachIndexed { index, text ->
                    Tab(selected = selectedTabIndex.value == index,
                        selectedContentColor = Color.White,
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
                    FavoriteScreenElement(state, goToPlayer)
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