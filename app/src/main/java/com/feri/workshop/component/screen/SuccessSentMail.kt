package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.feri.workshop.R
import com.feri.workshop.ui.helper.spacerV
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object SuccessSentMail : Screen {
    override val routeName = "SuccessSentMail"

    @Composable
    override fun screen(navController: NavHostController) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Email telah terkirim, buka link pada email &")
            Text(text = "masukkan password baru")
            spacerV(height = 16.dp)
            Button(onClick = {
                navController.navigate(Login.routeName){
                    popUpTo(Register.routeName) { inclusive = true }
                    launchSingleTop=true
                }
            }) {
                Text(text = "Login")
            }
        }
    }
}