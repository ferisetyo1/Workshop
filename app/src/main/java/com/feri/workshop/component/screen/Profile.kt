package com.feri.workshop.component.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.R

object Profile : Screen {
    override val name = "Profile"
    override val icon = R.drawable.ic_profile
    override val showBottomNav = true

    @ExperimentalAnimationApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val activity = context as MainActivity
        val mainVM = activity.mainViewModel
        Column {
            Button(onClick = { mainVM.logOut() }) {
                Text(text = "Logout")
            }
        }
    }
}