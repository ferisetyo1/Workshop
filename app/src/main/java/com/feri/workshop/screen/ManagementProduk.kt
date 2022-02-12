package com.feri.workshop.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.feri.workshop.R

object ManagementProduk : Screen {
    override val name = "ManagementProduk"
    override val showBottomNav = true
    override val icon: Int= R.drawable.ic_box

    @Composable
    override fun screen(navController: NavHostController) {

    }
}