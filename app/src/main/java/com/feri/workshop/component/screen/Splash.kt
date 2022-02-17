package com.feri.workshop.component.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

object Splash : Screen {
    override val name = "Splash"

    @ExperimentalAnimationApi
    @Composable
    override fun screen(navController: NavHostController) {
        LaunchedEffect(key1 = true) {
            delay(3000)
            navController.navigate(Beranda.name)
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Q-WORKSHOP APPS")
        }
    }
}