package com.feri.workshop.component.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity

interface Screen {
    val routeName: String
    val pageTitle: String
        get() = ""
    val icon: Int
        get() = -1
    val showBottomNav: Boolean
        get() = false

    @Composable
    fun screen(navController: NavHostController)

    @Composable
    fun getMainActivity(): MainActivity {
        val context= LocalContext.current
        return context as MainActivity
    }
}