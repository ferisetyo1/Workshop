package com.feri.workshop.component.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.R
import com.feri.workshop.ui.helper.spacerV
import kotlinx.coroutines.delay

object Splash : Screen {
    override val routeName = "Splash"

    @ExperimentalAnimationApi
    @Composable
    override fun screen(navController: NavHostController) {
        val mainVM=getMainActivity().mainViewModel
        val user by remember{mainVM.myuser}
        LaunchedEffect(key1 = true) {
            delay(3000)
            if (user?.active == true) navController.navigate(Beranda.routeName)
            else navController.navigate(SuccessRegister.routeName)
        }

        Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Text(text = "Q-WORKSHOP APPS",fontWeight = FontWeight.W600,fontSize = 20.sp)
            spacerV(height = 16.dp)
            Image(painter = painterResource(id = R.drawable.logo_light), contentDescription = "",modifier = Modifier.fillMaxWidth(0.7f))
        }
    }
}