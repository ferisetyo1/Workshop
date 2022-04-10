package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.data.model.KategoriProduk
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.showToast

object AddKategoriProduk : Screen {
    override val routeName = "addkategoriproduk"
    override val pageTitle = "Tambah Kategori"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val activity = context as MainActivity
        val produkVM = activity.produkViewModel

        var nama by remember { mutableStateOf("") }
        var errorNama by remember { mutableStateOf("") }

        val listKategory by remember { produkVM.kategoriProduk }

        var isLoading by remember { mutableStateOf(false) }

        val keyboard = LocalSoftwareKeyboardController.current
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = pageTitle)
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
            screenLoading(loading = isLoading)
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Text(text = "Nama")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        errorNama = ""
                        nama = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorNama.isNotEmpty(),
                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                if (errorNama.isNotEmpty()) Text(
                    text = errorNama,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Button(
                    onClick = {
                        keyboard?.hide()
                        if (nama.isEmpty()) {
                            errorNama = "Nama tidak boleh kosong."
                            return@Button
                        }
                        produkVM.addKategori(
                            kategori = KategoriProduk(
                                nama = nama,
                                createdBy = "feri"
                            ),
                            isLoading = { isLoading = it },
                            onSuccess = {
                                nama = ""
                                context.showToast("Berhasil menambahkan produk")
                            },
                            onFailed = { context.showToast(it) })
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Tambahkan")
                }
                spacerV(height = 16.dp)
                dividerSmallH()
                spacerV(height = 16.dp)
                Text(text = "List Kategori", fontSize = 16.sp, fontWeight = FontWeight.W700)
                spacerV(height = 16.dp)
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(listKategory) {
                        Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = it.nama.orEmpty(), fontWeight = FontWeight.W700)
                                IconButton(onClick = {
                                    produkVM.deleteKategori(
                                        kategori = it,
                                        isLoading = { isLoading = it },
                                        onSuccess = { context.showToast("Berhasil menghapus kategori") },
                                        onFailed = { context.showToast(it) })
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                        spacerV(height = 8.dp)
                    }
                }
            }
        }
    }
}