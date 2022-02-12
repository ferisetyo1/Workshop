package com.feri.workshop.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface Screen {
    val name: String
    val icon: Int
        get() = -1
    val showBottomNav: Boolean
        get() = false
    @Composable
    fun screen(navController: NavHostController)
}