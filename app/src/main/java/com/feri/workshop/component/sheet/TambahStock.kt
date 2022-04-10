package com.feri.workshop.component.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.data.model.Produk
import com.feri.workshop.data.model.Stock
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.showToast

object TambahStock : Sheets {
    override val routeName = "tambahstock"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val produkVM = getMainActivity().produkViewModel
        var stock by remember { mutableStateOf("") }
        var deskripsi by remember { mutableStateOf("") }
        var errorStock by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        val keyoboard=LocalSoftwareKeyboardController.current
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
            Text(text = "Tambah Stock",fontSize = 16.sp,fontWeight = FontWeight.W600)
            spacerV(height = 8.dp)
            Text(text = "Masukkan jumlah stock : ")
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = stock,
                onValueChange = {
                    errorStock = ""
                    stock = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                isError = errorStock.isNotEmpty(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            )
            if (errorStock.isNotEmpty()) Text(
                text = errorStock,
                color = Color.Red,
                fontSize = 12.sp
            )
            spacerV(height = 16.dp)
            Text(text = "Deskripsi")
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = deskripsi,
                onValueChange = {
                    deskripsi = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardActions = KeyboardActions(onDone = {keyoboard?.hide()}),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                )
            )
            spacerV(height = 16.dp)
            Button(
                onClick = {
                    if (stock.isEmpty()) {
                        errorStock = "Stock tidak boleh kosong."
                        return@Button
                    }
                    produkVM.addStock(
                        Stock(jumlah = stock.toLongOrNull() ?: 0,deskripsi = deskripsi),
                        isLoading = { isLoading = it },
                        onSuccess = {
                            context.showToast("Berhasil menambahkan stocks")
                            navController.navigateUp()
                        },
                        onFailed = { context.showToast("Gagal menambahkan stocks") })
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading
            ) {
                Text(text = "Simpan")
            }
            spacerV(height = 16.dp)
        }
//        screenLoading(loading = isLoading)
    }
}