package com.example.otiummusicplayer.ui.features.searchScreen.main.screenElements

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.ui.theme.WhiteTr

private const val PORTRAIT_CONF_ITEM_NUMBER = 2
private const val LANDSCAPE_CONF_ITEM_NUMBER = 4

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ShowArtistsAlbums(
    albums: List<AlbumModel>?,
    onClick: (id: String) -> Unit,
    onClose: () -> Unit
) {
    val orientation = LocalConfiguration.current.orientation

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        ),
        onDismissRequest = {
            onClose()
        },
        modifier = Modifier.fillMaxWidth(),
        containerColor = WhiteTr
    ) {
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
                        maxItemsInEachRow = if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                            PORTRAIT_CONF_ITEM_NUMBER
                        } else {
                            LANDSCAPE_CONF_ITEM_NUMBER
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
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
