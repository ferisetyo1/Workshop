package com.feri.workshop.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.showToast

object Login : Screen {
    override val name = "Login"

    @ExperimentalAnimationApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val activity = context as MainActivity
        val mainVM = activity.mainViewModel
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Welcome to Q-Workshop", fontSize = 20.sp, fontWeight = FontWeight.W700)
            spacerV(height = 32.dp)
            Text(
                text = "Username", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Black.copy(alpha = 0.25f))
            )
            spacerV(height = 16.dp)
            Text(
                text = "Password", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Black.copy(alpha = 0.25f))
            )
            spacerV(height = 32.dp)
            Button(
                onClick = {
                    mainVM.login(username, password, {
                        navController.navigate(Beranda.name) {
                            popUpTo(name) { inclusive = true }
                            launchSingleTop = true
                        }
                    }, {
                        context.showToast(message=it)
                    })
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = "Masuk")
            }
        }
    }
}