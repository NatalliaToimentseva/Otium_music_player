package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.screenElements

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
import androidx.compose.material3.MaterialTheme
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
import com.example.otiummusicplayer.models.networkPart.AlbumModel
import com.example.otiummusicplayer.ui.features.generalProjectsScreenElements.ShowProgress
import com.example.otiummusicplayer.utils.toast

@Composable
fun AlbumsList(data: LazyPagingItems<AlbumModel>?, onClick: (id: String) -> Unit) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 10.dp)
    ) {
        data?.let { items ->
            when (items.loadState.refresh) {
                is LoadState.Error -> LocalContext.current.toast(stringResource(id = R.string.loading_error))
                LoadState.Loading -> ShowProgress()
                else -> {
                    LazyRow {
                        items(items.itemCount) { index ->
                            Column(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(10.dp)
                                    .clip(shape = RoundedCornerShape(15.dp))
                                    .clickable {
                                        items[index]?.id?.let { onClick.invoke(it) }
                                    }
                            ) {
                                AsyncImage(
                                    model = items[index]?.image,
                                    placeholder = painterResource(id = R.drawable.no_image),
                                    error = painterResource(id = R.drawable.no_image),
                                    contentDescription = stringResource(id = R.string.album_im),
                                    modifier = Modifier
                                        .height(110.dp)
                                        .width(110.dp),
                                )
                                Text(
                                    text = items[index]?.name
                                        ?: stringResource(id = R.string.no_title),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .width(110.dp)
                                        .padding(top = 5.dp)
                                )
                                Text(
                                    text = items[index]?.artistName
                                        ?: stringResource(id = R.string.no_title),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary,
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