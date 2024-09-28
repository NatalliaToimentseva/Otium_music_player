package com.example.otiummusicplayer.ui.features.searchScreen.main.screenElements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.Hover
import com.example.otiummusicplayer.ui.theme.White

@Composable
fun BottomNavigationScreenElement() {
    val selectedIcon by remember {
        mutableStateOf("Home")
    }
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        containerColor = Graphite,
        contentColor = White
    ) {
        NavigationBarItem(
            alwaysShowLabel = false,
            selected = selectedIcon == "Home",
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = null,
                    modifier = Modifier.width(40.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Hover,
                selectedIconColor = Graphite,
                unselectedIconColor = White
            )
        )
        NavigationBarItem(
            alwaysShowLabel = false,
            selected = selectedIcon == "Other",
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    modifier = Modifier.width(40.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Hover,
                selectedIconColor = Graphite,
                unselectedIconColor = White
            )
        )
    }
}