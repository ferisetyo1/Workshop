package com.feri.workshop.ui.helper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun dividerSmallH(height: Dp) {
    Spacer(
        modifier = Modifier
            .height(height = height)
            .fillMaxWidth()
            .background(color = Color.White.copy(alpha = 0.5f))
    )
}