package com.example.otiummusicplayer.ui.features.generalScreenElements

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.navigation.Route
import com.example.otiummusicplayer.ui.theme.Hover
import com.example.otiummusicplayer.ui.theme.TealTr
import com.example.otiummusicplayer.ui.theme.WhiteUnselected
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BottomNavigationScreenElement(
    selectedRoute: Route,
    permissionsState: MultiplePermissionsState,
    navigate: (route: String) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = WhiteUnselected,
    ) {
        NavigationBarItem(
            alwaysShowLabel = true,
            selected = selectedRoute == Route.PlaylistsScreen,
            onClick = {
                navigate.invoke(Route.PlaylistsScreen.route)
            },
            icon = {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.playlist),
                    contentDescription = stringResource(id = R.string.playlist),
                    modifier = Modifier.width(30.dp)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.playlist),
                    fontSize = 12.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = TealTr,
                selectedTextColor = Hover,
                unselectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NavigationBarItem(
                alwaysShowLabel = true,
                selected = selectedRoute == Route.FoldersScreen,
                enabled = permissionsState.allPermissionsGranted,
                onClick = {
                    navigate.invoke(Route.FoldersScreen.route)
                },
                icon = {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.folders),
                        contentDescription = stringResource(id = R.string.folders),
                        modifier = Modifier.width(30.dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.folders),
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = TealTr,
                    selectedTextColor = Hover,
                    unselectedTextColor = MaterialTheme.colorScheme.primary
                )
            )
        }
        NavigationBarItem(
            alwaysShowLabel = true,
            selected = selectedRoute == Route.MobileStorageTracksScreen,
            enabled = permissionsState.allPermissionsGranted,
            onClick = {
                navigate.invoke(Route.MobileStorageTracksScreen.route)
            },
            icon = {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.tracks),
                    contentDescription = stringResource(id = R.string.tracks),
                    modifier = Modifier.width(30.dp)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.tracks),
                    fontSize = 12.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = TealTr,
                selectedTextColor = Hover,
                unselectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
        NavigationBarItem(
            alwaysShowLabel = true,
            selected = selectedRoute == Route.NetworkSearch,
            onClick = {
                navigate.invoke(Route.NetworkSearch.route)
            },
            icon = {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.network_search),
                    contentDescription = stringResource(id = R.string.search),
                    modifier = Modifier.width(30.dp)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.search),
                    fontSize = 12.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = TealTr,
                selectedTextColor = Hover,
                unselectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}