package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.screenElements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.EMPTY
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.NetworkSearchAction
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.NetworkSearchState

@Composable
fun SearchFieldScreenElement(
    state: NetworkSearchState,
    processAction: (action: NetworkSearchAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                processAction(NetworkSearchAction.SearchByQuery)
                focusManager.clearFocus()
            }
        ),
        isError = state.isSearchError,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.primary,
            errorPlaceholderColor = MaterialTheme.colorScheme.onErrorContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            errorIndicatorColor = MaterialTheme.colorScheme.onErrorContainer
        )
    )
}