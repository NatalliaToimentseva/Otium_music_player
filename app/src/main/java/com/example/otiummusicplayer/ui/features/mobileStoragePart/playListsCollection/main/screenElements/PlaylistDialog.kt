package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.screenElements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain.CollectionListAction
import com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain.CollectionListState
import com.example.otiummusicplayer.ui.theme.Hover
import com.example.otiummusicplayer.ui.theme.TealLight

@Composable
fun ShowPlaylistDialog(
    titleId: Int,
    placeholder: String?,
    state: CollectionListState,
    processAction: (action: CollectionListAction) -> Unit,
    onClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { processAction(CollectionListAction.HideDialog) },
        confirmButton = {
            TextButton(onClick = {
                onClick.invoke()
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
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = titleId),
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
                        processAction(
                            CollectionListAction.SetNewPlaylistTitleFromDialog(
                                title
                            )
                        )
                    },
                    placeholder = {
                        if(placeholder != null) {
                            Text(
                                text = placeholder,
                                style = TextStyle(fontSize = 16.sp),
                                color = Color.DarkGray
                            )
                        }
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