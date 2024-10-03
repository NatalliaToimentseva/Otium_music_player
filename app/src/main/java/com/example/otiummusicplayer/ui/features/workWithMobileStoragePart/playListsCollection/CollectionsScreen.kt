package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.generalScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain.CollectionListAction
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain.CollectionListState
import com.example.otiummusicplayer.ui.features.generalScreenElements.BottomNavigationScreenElement
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.permissionRequesElement.MultiplePermissionDialog
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.FloatingButton
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.Hover
import com.example.otiummusicplayer.ui.theme.TealLight
import com.example.otiummusicplayer.ui.theme.TealTr
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.utils.toast
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun CollectionListDestination(
    navHostController: NavHostController,
    viewModel: CollectionListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
//    val gson = GsonBuilder().create()
//    val tracks = state.tracks ?: arrayListOf()
//    val tracksList = gson.toJson(tracks)
    CollectionListScreen(
        state = state,
        processAction = viewModel::processAction,
        goToTracks = { itemId ->
//        navHostController.navigate(
//            Route.PlayTrackScreen.selectRoute(itemId = itemId, tracks = tracksList)
//        )
        },
        navigate = { route ->
            navHostController.navigate(route)
        })
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun CollectionListScreen(
    state: CollectionListState,
    processAction: (action: CollectionListAction) -> Unit,
    goToTracks: (idPlaylist: Long) -> Unit,
    navigate: (route: String) -> Unit
) {
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
    LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            processAction(CollectionListAction.IsShowPermissionDialog(true))
        }
    }
    val playlists = state.playLists?.collectAsLazyPagingItems()
    Scaffold(
        modifier = Modifier
            .background(Graphite)
            .fillMaxSize()
            .padding(10.dp),
        containerColor = Graphite,
        bottomBar = {
            BottomNavigationScreenElement(
                Route.PlaylistsScreen,
                permissionsState,
                navigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { processAction(CollectionListAction.ShowDialog) },
                shape = MaterialTheme.shapes.small.copy(CornerSize(40)),
                containerColor = FloatingButton,
                contentColor = White
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.floating_button)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.playlist),
                    fontSize = 26.sp,
                    color = White,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                )
                if (state.selectedItemsList.isNotEmpty()) {
                    IconButton(onClick = { processAction(CollectionListAction.DeletePlaylist) }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
            playlists?.let { lists ->
                when (lists.loadState.refresh) {
                    is LoadState.Error -> LocalContext.current.toast(stringResource(id = R.string.loading_error))
                    LoadState.Loading -> ShowProgress(null)
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(lists.itemCount) { index ->
                                lists[index]?.let { item ->
                                    Row(verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 2.dp)
                                            .background(
                                                if (state.selectedItemsList.contains(item)) {
                                                    TealTr
                                                } else {
                                                    Color.Transparent
                                                }
                                            )
                                            .padding(horizontal = 15.dp, vertical = 6.dp)
                                            .combinedClickable(
                                                onClick = {
                                                    goToTracks(item.id)
                                                },
                                                onLongClick = {
                                                    processAction(
                                                        CollectionListAction.SelectItem(
                                                            item
                                                        )
                                                    )
                                                }
                                            )
                                    ) {
                                        Image(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
                                            contentDescription = stringResource(id = R.string.playlist),
                                            modifier = Modifier.height(30.dp)
                                        )
                                        Text(
                                            text = item.playListTitle,
                                            fontSize = 20.sp,
                                            color = White,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (state.error != null) {
            LocalContext.current.toast(state.error)
            processAction(CollectionListAction.ClearError)
        }
        if (state.showPermissionDialog) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                MultiplePermissionDialog(permissionsState) { isShow ->
                    processAction(CollectionListAction.IsShowPermissionDialog(isShow))
                }
            }
        }
        if (state.isShowDialog) {
            AlertDialog(
                onDismissRequest = { processAction(CollectionListAction.HideDialog) },
                confirmButton = {
                    TextButton(onClick = {
                        state.dialogText.let { text ->
                            if (text.isNotBlank()) processAction(
                                CollectionListAction.AddPlaylist(text)
                            )
                        }
                        processAction(CollectionListAction.HideDialog)
                    }) {
                        Text(
                            text = stringResource(id = R.string.positive_button),
                            color = Hover
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { processAction(CollectionListAction.HideDialog) }) {
                        Text(
                            text = stringResource(id = R.string.negative_button),
                            color = Hover
                        )
                    }
                },
                title = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.dialog_title),
                            style = TextStyle(fontSize = 18.sp),
                            color = Hover
                        )
                        Text(
                            text = stringResource(id = R.string.dialog_message),
                            style = TextStyle(fontSize = 16.sp),
                            color = Hover,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        TextField(
                            value = state.dialogText,
                            onValueChange = { title ->
                                processAction(CollectionListAction.SetTitleFromDialog(title))
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = TealLight,
                                unfocusedContainerColor = TealLight,
                                focusedTextColor = Hover,
                                unfocusedTextColor = Hover,
                                focusedIndicatorColor = Hover,
                                unfocusedIndicatorColor = Hover,
                                cursorColor = Hover
                            )
                        )
                    }
                },
                containerColor = TealLight
            )
        }
    }
}

private fun checkPermission(context: Context, permission: List<String>): Boolean {
    var denied = 0
    for (p in permission) {
        if (ContextCompat.checkSelfPermission(
                context,
                p
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            denied++
        }
    }
    return denied == 0
}