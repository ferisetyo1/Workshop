package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.feri.workshop.ui.helper.focusModifier
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.showToast

object GantiPassword : Screen {
    override val routeName = "gantiPassword"

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val mainVM = getMainActivity().mainViewModel
        val keyboard = LocalSoftwareKeyboardController.current
        var passwordLama by remember { mutableStateOf("") }
        var passwordBaru by remember { mutableStateOf("") }
        var errorText by remember { mutableStateOf("") }
        var showPasswordLama by remember { mutableStateOf(false) }
        var showPasswordBaru by remember { mutableStateOf(false) }
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = "Ganti Password")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                },
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent,
                elevation = 0.dp,
            )
        }) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                if (errorText.isNotEmpty()) {
                    Text(text = errorText, color = Color.Red)
                    spacerV(height = 16.dp)
                }
                Text(text = "Password Lama")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = passwordLama,
                    onValueChange = {
                        passwordLama = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = if (showPasswordLama) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        val image = if (showPasswordLama)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff
                        IconButton(onClick = { showPasswordLama = !showPasswordLama }) {
                            Icon(imageVector = image, contentDescription = "")
                        }
                    }
                )
                spacerV(height = 16.dp)
                Text(text = "Password Baru")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = passwordBaru,
                    onValueChange = {
                        passwordBaru = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                    visualTransformation = if (showPasswordBaru) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        val image = if (showPasswordBaru)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff
                        IconButton(onClick = { showPasswordBaru = !showPasswordBaru }) {
                            Icon(imageVector = image, contentDescription = "")
                        }
                    }
                )
                spacerV(height = 16.dp)
                Button(onClick = {
                    errorText = ""
                    if (passwordLama.isEmpty()){
                        errorText="Password lama tidak boleh kosong"
                        return@Button
                    }
                    if (passwordBaru.isEmpty()){
                        errorText="Password lama tidak boleh kosong"
                        return@Button
                    }
                    mainVM.gantiPassword(
                        passwordLama,
                        passwordBaru,
                        {
                            context.showToast("Berhasil mengubah password")
                            navController.navigateUp()
                        },
                        {
                            errorText = it
                        })
                },modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Simpan")
                }
            }
        }
    }
}