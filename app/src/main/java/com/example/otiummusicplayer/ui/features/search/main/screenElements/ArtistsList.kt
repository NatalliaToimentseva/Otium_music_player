package com.example.otiummusicplayer.ui.features.search.main.screenElements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.ArtistModel
import com.example.otiummusicplayer.ui.features.generalScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.utils.toast

@Composable
fun ArtistsList(artists: LazyPagingItems<ArtistModel>?, onClick: (id: String) -> Unit) {
    Box(
        modifier = Modifier
            .background(Graphite)
            .padding(vertical = 10.dp)
    ) {
        artists?.let { items ->
            when (items.loadState.refresh) {
                is LoadState.Error -> LocalContext.current.toast(stringResource(id = R.string.loading_error))
                LoadState.Loading -> ShowProgress(null)
                else -> {
                    LazyRow {
                        items(items.itemCount) { ind ->
                            Column(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(10.dp)
                                    .clip(shape = RoundedCornerShape(15.dp))
                                    .clickable {
                                        items[ind]?.id?.let { onClick.invoke(it) }
                                    }
                            ) {
                                AsyncImage(
                                    model = items[ind]?.image,
                                    placeholder = painterResource(id = R.drawable.no_image),
                                    error = painterResource(id = R.drawable.no_image),
                                    contentDescription = stringResource(id = R.string.album_im),
                                    modifier = Modifier
                                        .height(110.dp)
                                        .width(110.dp),
                                )
                                Text(
                                    text = items[ind]?.name
                                        ?: stringResource(id = R.string.no_title),
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
        }
    }
}