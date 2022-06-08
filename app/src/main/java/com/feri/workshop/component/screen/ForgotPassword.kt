package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.feri.workshop.component.viewmodel.LoginViewModel
import com.feri.workshop.ui.helper.focusModifier
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.showToast

object ForgotPassword : Screen {
    override val routeName = "ForgotPassword"

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun screen(navController: NavHostController) {
        val loginVM: LoginViewModel = hiltViewModel()
        val context = LocalContext.current
        var email by remember { mutableStateOf("") }
        var emailError by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var showPassword by remember { mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current
        screenLoading(loading = isLoading)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Lupa Password", fontSize = 20.sp, fontWeight = FontWeight.W700)
            spacerV(height = 32.dp)

            Text(
                text = "Email", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = email,
                onValueChange = {
                    emailError = ""
                    email = it
                },
                modifier = focusModifier()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                isError = emailError.isNotEmpty(),
                keyboardActions = KeyboardActions(onDone = {keyboardController?.hide()}),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            if (emailError.isNotEmpty()) Text(
                text = emailError, color = Color.Red, fontSize = 12.sp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            spacerV(height = 32.dp)

            Button(onClick = {
                if (email.isEmpty()) {
                    emailError = "Email tidak boleh kosong"
                    return@Button
                }
                loginVM.resetPassword(
                    email = email,
                    onSuccess = { navController.navigate(SuccessSentMail.routeName) },
                    onFailed = { context.showToast(it) })
            }) {
                Text(text = "Reset Password")
            }
        }
    }
}