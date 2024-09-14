package com.example.otiummusicplayer.ui.features.search.screens.main.screenElements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.network.entities.ArtistData
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.White

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArtistsList(artists: List<ArtistData>, onClick: (id: String) -> Unit) {
    Box(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .background(Graphite)
            .padding(vertical = 10.dp)
    ) {
        FlowColumn(
            maxItemsInEachColumn = 2
        ) {
            artists.forEach { itemsValue ->
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .clickable {
                            onClick.invoke(itemsValue.id)
                        }
                ) {
                    AsyncImage(
                        model = itemsValue.image,
                        placeholder = painterResource(id = R.drawable.no_image),
                        error = painterResource(id = R.drawable.no_image),
                        contentDescription = stringResource(id = R.string.album_im),
                        modifier = Modifier
                            .height(110.dp)
                            .width(110.dp),
                    )
                    Text(
                        text = itemsValue.name,
                        fontSize = 18.sp,
                        color = White,
                        modifier = Modifier
                            .width(110.dp)
                    )
                }
            }
        }
    }
}