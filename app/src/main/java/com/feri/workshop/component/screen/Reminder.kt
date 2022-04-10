package com.feri.workshop.component.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.feri.workshop.R

object Reminder : Screen {
    override val routeName = "Reminder"
    override val icon= R.drawable.ic_reminder
    override val showBottomNav = true

    @Composable
    override fun screen(navController: NavHostController) {

    }
}