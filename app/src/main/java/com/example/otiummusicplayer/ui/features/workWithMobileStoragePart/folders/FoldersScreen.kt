package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.generalScreenElements.BottomNavigationScreenElement
import com.example.otiummusicplayer.ui.features.generalScreenElements.ShowProgress
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.domain.FoldersAction
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.folders.domain.FoldersState
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.White
import com.example.otiummusicplayer.utils.toast
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun FoldersScreenDestination(
    navHostController: NavHostController,
    viewModel: FoldersScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    FoldersScreen(
        state = state,
        processAction = viewModel::processAction,
        goTracks = { folderId ->
            //TODO
        },
        navigate = { route ->
            navHostController.navigate(route)
        })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FoldersScreen(
    state: FoldersState,
    processAction: (action: FoldersAction) -> Unit,
    goTracks: (id: Int) -> Unit,
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
    Scaffold(
        modifier = Modifier
            .background(Graphite)
            .fillMaxSize()
            .padding(all = 10.dp),
        containerColor = Graphite,
        bottomBar = {
            BottomNavigationScreenElement(
                Route.FoldersScreen,
                permissionsState,
                navigate
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.padding(15.dp)
            ) {
                items(items = state.folders?.toList() ?: arrayListOf()) { item ->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            goTracks.invoke(item.id)
                        }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_music_folder),
                            contentDescription = stringResource(id = R.string.folders),
                        )
                        Text(
                            text = item.title,
                            fontSize = 20.sp,
                            color = White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
            if (state.isLoading) {
                ShowProgress(null)
            }
            if (state.error != null) {
                LocalContext.current.toast(state.error)
                processAction(FoldersAction.ClearError)
            }
        }
    }
}