package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

object SuccessRegister : Screen {
    override val routeName = "SuccessRegister"

    @Composable
    override fun screen(navController: NavHostController) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Silahkan hubungi admin,")
            Text(text = "untuk mengaktivkan akun anda")
            spacerV(height = 16.dp)
            Button(onClick = {
                Firebase.auth.signOut()
            }) {
                Text(text = "Login")
            }
        }
    }
}