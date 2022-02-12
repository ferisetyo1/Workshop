package com.feri.workshop.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.feri.workshop.R

object Reminder : Screen {
    override val name = "Reminder"
    override val icon= R.drawable.ic_reminder
    override val showBottomNav = true

    @Composable
    override fun screen(navController: NavHostController) {

    }
}