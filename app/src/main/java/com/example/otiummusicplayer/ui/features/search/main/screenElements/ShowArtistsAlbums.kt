package com.example.otiummusicplayer.ui.features.search.main.screenElements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ShowArtistsAlbums(
    albums: List<AlbumModel>?,
    onClick: (id: String) -> Unit,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        ),
        onDismissRequest = {
            onClose()
        }) {
        Scaffold(
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .background(Graphite)
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceAround,
                        maxItemsInEachRow = 2,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        albums?.let {
                            repeat(it.size) { ind ->
                                Column(
                                    modifier = Modifier.clickable {
                                        albums[ind].id.let { onClick.invoke(it) }
                                        onClose()
                                    }
                                ) {
                                    AsyncImage(
                                        model = albums[ind].image,
                                        contentDescription = albums[ind].name,
                                        placeholder = painterResource(id = R.drawable.no_image),
                                        error = painterResource(id = R.drawable.no_image),
                                        modifier = Modifier
                                            .height(160.dp)
                                            .width(160.dp)
                                            .padding(10.dp)
                                    )
                                    Text(
                                        text = albums[ind].name,
                                        fontSize = 18.sp,
                                        color = White,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
