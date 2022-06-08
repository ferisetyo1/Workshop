package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.ui.helper.focusModifier
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.PrimaryColor

object FindCustomer : Screen {
    override val routeName = "FindCustomer"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val createTransaksiViewModel = getMainActivity().transaksiViewModel
        var nomortelfon by remember { mutableStateOf("") }
        var nomorpolisi by remember { mutableStateOf("") }
        var errorText by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        val keyboard = LocalSoftwareKeyboardController.current
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = "Data Pelanggan")
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
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                Arrangement.Center
            ) {
                Card(
                    shape = CircleShape,
                    backgroundColor = PrimaryColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.PersonOutline,
                            contentDescription = "",
                            modifier = Modifier
                                .size(48.dp),
                            tint = Color.White
                        )
                    }
                }
                spacerV(height = 16.dp)
                if (errorText.isNotEmpty()) Text(text = errorText, fontSize = 12.sp)
                spacerV(height = 16.dp)
                Text(text = "Nomor Telepon")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = nomortelfon,
                    onValueChange = {
                        errorText = ""
                        nomortelfon = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorText.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                spacerV(height = 16.dp)
                Text(text = "Nomor Polisi")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = nomorpolisi,
                    onValueChange = {
                        errorText = ""
                        nomorpolisi = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorText.isNotEmpty(),
                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                spacerV(height = 32.dp)
                Button(
                    onClick = {
                        createTransaksiViewModel.searchCustomer(
                            notelp = nomortelfon,
                            nopol = nomorpolisi,
                            isLoading = {isLoading=true},
                            onSuccess = {navController.navigate(ListProduk.routeName)},
                            onFailed = {errorText=it})
                    }, shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Lanjutkan")
                }
            }
            screenLoading(loading = isLoading)
        }
    }
}