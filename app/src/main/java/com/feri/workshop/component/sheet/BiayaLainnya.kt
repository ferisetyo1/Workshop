package com.feri.workshop.component.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.ui.helper.focusModifier
import com.feri.workshop.ui.helper.spacerV

object BiayaLainnya : Sheets {
    override val routeName = "biayalainnya"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val createTransaksiViewModel = getMainActivity().transaksiViewModel
        var nama by remember { mutableStateOf("") }
        var errorNama by remember { mutableStateOf("") }
        var value by remember { mutableStateOf("") }
        var errorValue by remember { mutableStateOf("") }
        val keyboard = LocalSoftwareKeyboardController.current
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            spacerV(height = 8.dp)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(8.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally)
            )
            spacerV(height = 16.dp)
            Text(text = "Tambah Biaya Lainnya", fontSize = 16.sp, fontWeight = FontWeight.W600)
            spacerV(height = 12.dp)
            Row {
                Text(text = "Nama")
                Text(text = "*", color = Color.Red)
            }
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = nama,
                onValueChange = {
                    errorNama = ""
                    nama = it.uppercase()
                },
                modifier = focusModifier()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                isError = errorNama.isNotEmpty(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            if (errorNama.isNotEmpty()) Text(
                text = errorNama,
                color = Color.Red,
                fontSize = 12.sp
            )
            spacerV(height = 16.dp)
            Row {
                Text(text = "Nilai")
                Text(text = "*", color = Color.Red)
            }
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = value,
                onValueChange = {
                    errorValue = ""
                    value = it
                },
                modifier = focusModifier()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                isError = errorValue.isNotEmpty(),
                keyboardActions = KeyboardActions(onDone = {keyboard?.hide()}),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,keyboardType = KeyboardType.Number)
            )
            if (errorValue.isNotEmpty()) Text(
                text = errorValue,
                color = Color.Red,
                fontSize = 12.sp
            )
            spacerV(height = 16.dp)
            Button(
                onClick = {
                    if (nama.isEmpty()) {
                        errorNama = "Nama tidak boleh kosong."
                        return@Button
                    }
                    if (value.isEmpty()) {
                        errorValue = "Nilai tidak boleh kosong."
                        return@Button
                    }
                    createTransaksiViewModel.addBiayaLainnya(nama,value.toDoubleOrNull()?:0.0)
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(text = "Simpan")
            }
            spacerV(height = 16.dp)
        }
    }
}