package com.feri.workshop.ui.helper

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun permission(list: List<String>, contentwarning: @Composable ()->Unit={}, content: @Composable () -> Unit={}) {
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }
    var locpermission = rememberMultiplePermissionsState(
        permissions = list
    )
    when {
        locpermission.allPermissionsGranted -> {
            content()
        }
        else -> {
            contentwarning()
            LaunchedEffect(key1 = true) {
                locpermission.launchMultiplePermissionRequest()
            }
        }
    }

}