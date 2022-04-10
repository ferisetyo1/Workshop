package com.feri.workshop.ui.helper

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun screenLoading(loading: Boolean) {
    if (loading) Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {  }
            .defaultMinSize(300.dp, 200.dp)
            .background(color = Color.Black.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            Text(text = "Harap tunggu...")
        }
    }
}