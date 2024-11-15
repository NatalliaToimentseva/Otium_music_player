package com.example.otiummusicplayer.ui.features.mobileStoragePart.permissionRequesElement

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.otiummusicplayer.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

private const val SCHEME = "package"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiplePermissionDialog(
    permissionsState: MultiplePermissionsState,
    showDialog: (showDialog: Boolean) -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            showDialog(false)
        },
        title = {
            Text(
                text = stringResource(id = R.string.permission),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.permission_request),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    showDialog(false)
                    if (!permissionsState.shouldShowRationale) {
                        permissionsState.launchMultiplePermissionRequest()
                    } else {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts(SCHEME, context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(context, intent, null)
                    }
                }) {
                Text(
                    stringResource(id = R.string.positive_button),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog(false)
                }) {
                Text(
                    stringResource(id = R.string.negative_button),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            }
        },
    )
}