package com.feri.workshop.ui.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun focusModifier(direction:FocusDirection= FocusDirection.Down): Modifier {
    val focusManager = LocalFocusManager.current
    return Modifier.onKeyEvent {
        if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_TAB){
            focusManager.moveFocus(FocusDirection.Down)
            true
        }
        false
    }
}