package com.example.otiummusicplayer.ui.features.searchScreen.main.screenElements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.searchScreen.main.domain.EMPTY
import com.example.otiummusicplayer.ui.features.searchScreen.main.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.searchScreen.main.domain.NetworkSearchState
import com.example.otiummusicplayer.ui.theme.ErrorRed
import com.example.otiummusicplayer.ui.theme.Graphite
import com.example.otiummusicplayer.ui.theme.White

@Composable
fun SearchFieldScreenElement(
    state: NetworkSearchState,
    processAction: (action: NetworkSearchAction) -> Unit,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        placeholder = {
            Text(
                text = if (state.isSearchError) {
                    stringResource(id = R.string.searchError)
                } else stringResource(id = R.string.search)
            )
        },
        value = state.searchValue,
        onValueChange = { value ->
            processAction(NetworkSearchAction.SetSearchValue(value))
        },
        leadingIcon = {
            IconButton(onClick = { processAction(NetworkSearchAction.SearchByQuery) }) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            if (state.searchValue.isNotEmpty()) {
                IconButton(onClick = {
                    processAction(NetworkSearchAction.SetSearchValue(EMPTY))
                    processAction(NetworkSearchAction.ClearSearchResult)
                }) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.btn_close),
                        contentDescription = null
                    )
                }
            }
        },
        isError = state.isSearchError,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = White,
            errorTextColor = ErrorRed,
            unfocusedTextColor = White,
            focusedContainerColor = Graphite,
            errorContainerColor = Graphite,
            unfocusedContainerColor = Graphite,
            cursorColor = White,
            focusedIndicatorColor = White
        )
    )
}