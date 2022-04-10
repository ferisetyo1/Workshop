package com.feri.workshop.component.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.feri.workshop.component.viewmodel.LoginViewModel
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.isValidEmail
import com.feri.workshop.utils.showToast

object Login : Screen {
    override val routeName = "Login"

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @Composable
    override fun screen(navController: NavHostController) {
        val loginVM: LoginViewModel = hiltViewModel()
        val context = LocalContext.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf("") }
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
            Text(text = "Welcome to Q-Workshop", fontSize = 20.sp, fontWeight = FontWeight.W700)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                isError = emailError.isNotEmpty(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            if (emailError.isNotEmpty()) Text(
                text = emailError, color = Color.Red, fontSize = 12.sp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                onValueChange = {
                    passwordError = ""
                    password = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                isError = passwordError.isNotEmpty(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val image = if (showPassword)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(imageVector = image, contentDescription = "")
                    }
                }
            )
            if (passwordError.isNotEmpty()) Text(
                text = passwordError, color = Color.Red, fontSize = 12.sp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            spacerV(height = 32.dp)
            Button(
                onClick = {
                    if (email.isEmpty()) {
                        emailError = "Email tidak boleh kosong"
                        return@Button
                    }
                    if (!email.isValidEmail()) {
                        emailError = "Email tidak valid"
                        return@Button
                    }
                    if (password.isEmpty()) {
                        passwordError = "Password tidak boleh kosong"
                        return@Button
                    }
                    loginVM.login(
                        email = email,
                        password = password,
                        onSuccess = {
                            navController.navigate(Beranda.routeName) {
                                popUpTo(routeName) { inclusive = true }
                                launchSingleTop = true
                            }
                        }, onError = {
                            context.showToast(message = it)
                        }, onErrorEmail = {
                            emailError = it
                        }, onErrorPassword = {
                            passwordError = it
                        }, isLoading = {
                            isLoading = it
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