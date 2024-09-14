package com.example.otiummusicplayer.ui.features.search.screens.tracks

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.White

@Composable
fun ShowTrackList(
    navHostController: NavHostController,
    id: String,
    viewModel: TrackListViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getTrByAlb(id)
    }
    val tracksData by viewModel.tracksData.collectAsState()
    Column(
        modifier = Modifier
            .background(Graphite)
            .fillMaxSize()
            .padding(20.dp)
    ) {
        IconButton(onClick = {
            navHostController.popBackStack(
//            route = "NetworkSearchScreen",
//            inclusive = false
            )
        }) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.btn_back),
                contentDescription = "Back button",
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
        LazyColumn {
            items(items = tracksData?.tracks ?: arrayListOf()) { item ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navHostController.navigate(
                            "PlayTrackScreen?imgUrl=${Uri.encode(tracksData?.image)}&title=${Uri.encode(item.name)}&audioUrl=${Uri.encode(item.audio)}"
                        )
                    }) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
                        contentDescription = "Play composition",
                    )
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        color = White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}